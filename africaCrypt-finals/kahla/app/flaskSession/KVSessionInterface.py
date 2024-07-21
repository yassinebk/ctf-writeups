import base64
# import base64
import pickle
from flask import current_app

from flask.sessions import SessionInterface
from itsdangerous import BadSignature, Signer
from app.flaskSession import KVSession, SessionID


class KVSessionInterface(SessionInterface):
    pickleMethod = pickle
    kvSession = KVSession

    def open_session(self, app, request):
        key = app.secret_key

        if key is not None:
            session_cookie = request.cookies.get(
                app.config['SESSION_COOKIE_NAME'], None)

            s = None

            if session_cookie:
                try:
                    print("[+] Opening Session", session_cookie)
                    sid_s = Signer(app.secret_key).unsign(
                        session_cookie).decode('ascii')
                    sid = SessionID.unserialize(sid_s)

                    if sid.has_expired(app.permanent_session_lifetime):
                        raise KeyError

                    print("[+] Session ID ", current_app.kvession_store.get(sid_s))
                    s = self.kvSession(
                            # CAN WE INJECT THIS VALUE ? 
                            self.pickleMethod.loads(base64.b64decode( current_app.kvsession_store.get(sid_s)))
                        )
                    s.sid_s = sid_s
                except (BadSignature, KeyError):
                    pass

            if s is None:
                s = self.kvSession()  # create an empty session
                s.new = True

            return s

    def save_session(self, app, session, response):
        # we only save modified sessions
        if session.modified:

            if not getattr(session, 'sid_s', None):
                print("[+] Generating Session ID",app.config['SESSION_RANDOM_SOURCE'])
                session.sid_s = SessionID(
                    current_app.config['SESSION_RANDOM_SOURCE'].getrandbits(
                        app.config['SESSION_KEY_BITS'])).serialize()
            
            print("[+] Saving Session", session.sid_s)

            # save the session, now its no longer new (or modified)
            data = self.pickleMethod.dumps(dict(session))
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



