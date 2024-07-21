const express = require('express')
const session = require('express-session')
const crypto = require('crypto')
const fs = require('fs')
const fsp = require('fs/promises')
const yaml = require('js-yaml')
const cp = require('child_process')
const http = require('http')
const path = require('path')



const GUEST_PRIVILEGE = yaml.load(fs.readFileSync('test.yaml', 'utf8'))
console.log(GUEST_PRIVILEGE,GUEST_PRIVILEGE.admin)
