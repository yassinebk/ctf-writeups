from datetime import datetime
from random import SystemRandom
import re

from flask import current_app

from app.flaskSession import KVSessionInterface

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