from flask import current_app
from flask.sessions import SessionMixin
from more_itertools import callback_iter
from flask.sessions import SessionMixin
from werkzeug.datastructures import CallbackDict

# The SESSION IS ACTUALLY A KEY VALUE STORE
class KVSession(callback_iter, SessionMixin):
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


