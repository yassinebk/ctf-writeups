# Inspired from Kvsession python library to implement server side based session interface
import calendar, base64
try:
    import cPickle as pickle
except ImportError:
    import pickle
from datetime import datetime
from random import SystemRandom
import re

from flask import current_app
from flask.sessions import SessionMixin, SessionInterface
from itsdangerous import Signer, BadSignature
from werkzeug.datastructures import CallbackDict


class SessionID(object):

    def __init__(self, id, created=None):
        if None == created:
            created = datetime.utcnow()

        self.id = id
        self.created = created

    def has_expired(self, lifetime, now=None):
        now = now or datetime.utcnow()
        return now > self.created + lifetime

    def serialize(self):
        return '%x_%x' % (self.id, calendar.timegm(self.created.utctimetuple())
                          )

    @classmethod
    def unserialize(cls, string):
        id_s, created_s,*opts = string.split('_')
        return cls(int(id_s, 16),
                   datetime.utcfromtimestamp(int(created_s, 16)))


class KVSession(CallbackDict, SessionMixin):
    modified = False
    def __init__(self, initial=None):
        def _on_update(d):
            d.modified = True

        CallbackDict.__init__(self, initial, _on_update)

    def destroy(self):
        for k in list(self.keys()):
            del self[k]

        if getattr(self, 'sid_s', None):
            current_app.kvsession_store.delete(self.sid_s)
            self.sid_s = None

        self.modified = False
        self.new = False

    def regenerate(self):
        self.modified = True

        if getattr(self, 'sid_s', None):
            # delete old session
            current_app.kvsession_store.delete(self.sid_s)

            # remove sid_s, set modified
            self.sid_s = None
            self.modified = True

            # save_session() will take care of saving the session now


class KVSessionInterface(SessionInterface):
    pickle_method = pickle
    session_class = KVSession

    def open_session(self, app, request):
        key = app.secret_key

        if key is not None:
            session_cookie = request.cookies.get(
                app.config['SESSION_COOKIE_NAME'], None)

            s = None

            if session_cookie:
                try:
                    sid_s = Signer(app.secret_key).unsign(
                        session_cookie).decode('ascii')
                    sid = SessionID.unserialize(sid_s)

                    if sid.has_expired(app.permanent_session_lifetime):
                        raise KeyError

                    s = self.session_class(self.pickle_method.loads(base64.b64decode(
                        current_app.kvsession_store.get(sid_s))))
                    s.sid_s = sid_s
                except (BadSignature, KeyError):
                    pass

            if s is None:
                s = self.session_class()  # create an empty session
                s.new = True

            return s

    def save_session(self, app, session, response):
        # we only save modified sessions
        if session.modified:
            if not getattr(session, 'sid_s', None):
                session.sid_s = SessionID(
                    current_app.config['SESSION_RANDOM_SOURCE'].getrandbits(
                        app.config['SESSION_KEY_BITS'])).serialize()

            # save the session, now its no longer new (or modified)
            print(dict(session))
            data = self.pickle_method.dumps(dict(session))
            store = current_app.kvsession_store
            store.put(session.sid_s, base64.b64encode(data))

            session.new = False
            session.modified = False

            # save sid_s in cookie
            cookie_data = Signer(app.secret_key).sign(
                session.sid_s.encode('ascii'))

            response.set_cookie(key=app.config['SESSION_COOKIE_NAME'],
                                value=cookie_data,
                                expires=self.get_expiration_time(app, session),
                                path=self.get_cookie_path(app),
                                domain=self.get_cookie_domain(app),
                                secure=app.config['SESSION_COOKIE_SECURE'],
                                httponly=app.config['SESSION_COOKIE_HTTPONLY'])


class KVSessionExtension(object):
    key_regex = re.compile('^[0-9a-f]+_[0-9a-f]+$')

    def __init__(self, session_kvstore=None, app=None):
        self.default_kvstore = session_kvstore

        if app and session_kvstore:
            self.init_app(app)

    def cleanup_sessions(self, app=None):
        if not app:
            app = current_app
        for key in app.kvsession_store.keys():
            m = self.key_regex.match(key)
            now = datetime.utcnow()
            if m:
                # read id
                sid = SessionID.unserialize(key)

                # remove if expired
                if sid.has_expired(app.permanent_session_lifetime, now):
                    app.kvsession_store.delete(key)

    def init_app(self, app, session_kvstore=None):
        app.config.setdefault('SESSION_KEY_BITS', 64)
        app.config.setdefault('SESSION_RANDOM_SOURCE', SystemRandom())

        if not session_kvstore and not self.default_kvstore:
            raise ValueError('Must supply session_kvstore either on '
                             'construction or init_app().')

        app.kvsession_store = session_kvstore or self.default_kvstore

        app.session_interface = KVSessionInterface()
