import os, random, platform
from datetime import datetime
from flask import jsonify
from PIL import Image

regions = [
    "Dominion Stronghold",
    "Elite Enclave",
    "Shadow Nexus",
    "Ironheart Citadel",
    "Obsidian Bastion",
    "Sovereign Terrace",
    "Tyrant's Domain",
    "Apex Summit",
    "Citadel of Supremacy",
    "Imperial Precinct",
    "Regal Bastion",
    "Dominus Outpost",
    "Majesty Court",
    "Vanguard Stronghold",
    "Elite's Embrace"
]

avs = [
    "Avira",
    "ESET",
    "Kaspersky",
    "Sophos",
    "BitDefender",
    "N/A"
]

arches = [
    "x32",
    "x64"
]

platforms = [
    "darwin",
    "linux",
    "win32"
]

screenshots = [
    "/static/uploads/macos.png",
    "/static/uploads/linux.png",
    "/static/uploads/windows.png"
]

permission_types = [
    "user",
    "root"
]

macos_hostnames = [
    "mac-desktop",
    "apple-pc",
    "mac-workstation",
    "macbook-pro",
    "imac-pro",
    "mac-server",
    "xcode-dev",
    "swift-machine",
    "creative-studio",
    "mac-mini",
    "mac-explorer",
    "apple-tower",
    "mac-guru",
    "sysadmin-sanctum",
    "server-haven",
    "cloud-kingdom",
    "terminal-elite",
    "mac-tower",
    "digital-oasis",
    "data-sanctuary",
    "network-dome",
    "script-maestro",
    "secure-vault",
    "vmware-host",
    "artistry-station",
    "design-wizard",
    "sysadmin-refuge",
    "devops-nirvana",
    "mac-cluster",
    "macbook-air",
    "terminal-maestro",
    "cyber-sorcerer",
    "virtual-mac",
    "web-server",
    "code-artisan",
    "command-hub",
    "mac-maestro",
    "data-factory",
    "tech-studio",
    "digital-mind",
    "mac-pro",
    "macbook",
    "imac-workstation",
    "macbook-air",
    "mac-mini-server",
    "mac-dev",
    "mac-hackerbox",
    "mac-lab",
    "apple-machine",
    "mac-magic"
]

linux_hostnames = [
    "linux-desktop",
    "penguin-pc",
    "tux-workstation",
    "penguin-laptop",
    "linux-server",
    "penguin-dev",
    "open-source-pc",
    "kernel-machine",
    "terminal-box",
    "command-central",
    "code-ninja",
    "hacker-haven",
    "linux-guru",
    "sysadmin-pc",
    "server-farm",
    "cloud-node",
    "bash-box",
    "penguin-tower",
    "cyber-cube",
    "data-fusion",
    "network-hub",
    "script-master",
    "secure-vault",
    "docker-host",
    "machine-learning",
    "hacking-station",
    "sysadmin-lair",
    "devops-rig",
    "linux-cluster",
    "raspberry-pi",
    "terminal-jockey",
    "cyber-dragon",
    "virtual-machine",
    "web-server",
    "code-warrior",
    "command-line",
    "linux-superstar",
    "data-center",
    "tech-lab",
    "binary-brain",
    "ubuntu-desktop",
    "debian-pc",
    "arch-workstation",
    "fedora-laptop",
    "centos-server",
    "gentoo-dev",
    "mint-machine",
    "kali-hackerbox",
    "redhat-lab",
    "elementary-box"
]

windows_hostnames = [
    "windows-desktop",
    "microsoft-pc",
    "win-workstation",
    "windows-laptop",
    "pc-gamer",
    "windows-server",
    "redmond-dev",
    "azure-machine",
    "home-office-pc",
    "code-central",
    "win-explorer",
    "tech-tower",
    "windows-guru",
    "sysadmin-palace",
    "server-hive",
    "cloud-master",
    "cmd-box",
    "windows-tower",
    "cyber-fortress",
    "data-empire",
    "network-gateway",
    "script-wizard",
    "safe-haven",
    "virtual-box",
    "ai-workstation",
    "hack-station",
    "sysadmin-sanctuary",
    "devops-lab",
    "win-cluster",
    "surface-pro",
    "powershell-jockey",
    "cyber-wizard",
    "virtual-pc",
    "web-host",
    "code-hero",
    "command-center",
    "windows-maestro",
    "data-factory",
    "tech-hub",
    "digital-mind",
    "win-desktop",
    "lumia-pc",
    "surface-workstation",
    "dell-laptop",
    "hp-server",
    "asus-dev",
    "acer-machine",
    "lenovo-hackerbox",
    "intel-lab",
    "alienware-box"
]
        
generate = lambda x: os.urandom(x).hex()

def generate_random_unix_timestamp():
    start_date = datetime(2023, 1, 1)
    end_date = datetime.now()
    start_timestamp = int(start_date.timestamp())
    end_timestamp = int(end_date.timestamp())
    random_timestamp = random.randint(start_timestamp, end_timestamp)
    return datetime.fromtimestamp(random_timestamp)


def response(message):
    return jsonify({"message": message})


def machine_info():
    information = {}
    information["os"] = platform.system()
    information["hostname"] = platform.node()
    information["machine"] = platform.machine()
    return information


def allowed_file(filename, allowed):
    return "." in filename and filename.rsplit(".", 1)[1].lower() in allowed


def check_img(filename):
    try:
        im = Image.open(filename)
        im.verify()
        im.close()
        im = Image.open(filename) 
        im.transpose(Image.FLIP_LEFT_RIGHT)
        im.close()
        return True
    except: 
        return False


def build_implant(implant_path, implant_file, server_url):
    # Generating an ID
    implant_id = generate(32)
    # Setting the build directory for the implant
    new_build_dir = f"/tmp/{implant_id}"

    # Creating the new directory <- #SAFE
    os.mkdir(new_build_dir)
    # #SAFE
    os.system(f"cp {implant_path}/* {new_build_dir}")

    implant_file = open(f"{new_build_dir}/{implant_file}", "r")
    implant_src = implant_file.read()
    implant_file.close()
    implant_src = implant_src.replace("SERVER_URL", server_url)

    new_src_path = f"/{new_build_dir}/{implant_id}.go"
    new_src_file = open(new_src_path, "w")
    new_src_file.write(implant_src)
    new_src_file.close()

    # Generating the new implant
    os.system(f"go generate -x {new_src_path}")
    os.system(f"go build -C {new_build_dir} -o {new_build_dir}/{implant_id} {new_src_path}")

    return f"{new_build_dir}/{implant_id}"