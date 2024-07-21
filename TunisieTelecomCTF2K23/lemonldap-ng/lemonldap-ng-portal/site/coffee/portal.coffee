###
LemonLDAP::NG Portal jQuery scripts
###

# Translation mechanism

translationFields = {}

# Launched at startup: download language JSON and translate all HTML tags that
# contains one of the following attributes using translate() function:
#  - trspan       : set result in tag content
#  - trmsg        : get error number and set result of PE<number> result in tag
#                   content
#  - trplaceholder: set result in "placeholder" attribute
#  - localtime    : transform time (in ms)ing translate()

setDanger = (cond, field) ->
	result = false
	if cond
		$("##{field}").addClass 'fa-check text-success'
		$("##{field}").removeClass 'fa-times text-danger'
		$("##{field}").attr 'role', 'status'
	else
		$("##{field}").addClass 'fa-times text-danger'
		$("##{field}").removeClass 'fa-check text-success'
		$("##{field}").attr 'role', 'alert'
		result = true
	result

translatePage = (lang) ->
	$.getJSON "#{window.staticPrefix}languages/#{lang}.json", (data) ->
		translationFields = data
		for k,v of window.datas.trOver.all
			translationFields[k] = v
		if window.datas.trOver[lang]
			for k,v of window.datas.trOver[lang]
				translationFields[k] = v
		$("[trspan]").each ->
			args = $(this).attr('trspan').split(',')
			txt = translate args.shift()
			for v in args
				txt = txt.replace /%[sd]/, v
			$(this).html txt
		$("[trmsg]").each ->
			$(this).html translate "PE#{$(this).attr 'trmsg'}"
			msg = translate "PE#{$(this).attr 'trmsg'}"
			if msg.match /_hide_/
				$(this).parent().hide()
		$("[trplaceholder]").each ->
			tmp = translate($(this).attr('trplaceholder'))
			$(this).attr 'placeholder', tmp
			$(this).attr 'aria-label', tmp
		$("[localtime]").each ->
			d = new Date $(this).attr('localtime') * 1000
			$(this).text d.toLocaleString()

# Translate a string
translate = (str) ->
	return if translationFields[str] then translationFields[str] else str

window.translate = translate

# Initialization variables: read all <script type="application/init"> tags and
# return JSON parsing result. This is set in window.data variable
getValues = () ->
	values = {}
	$("script[type='application/init']").each ->
		try
			tmp = JSON.parse $(this).text()
			for k of tmp
				values[k] = tmp[k]
		catch e
			console.log 'Parsing error', e
			console.log 'JSON', $(this).text()
	console.log values
	values

# Gets a query string parametrer
# We cannot use URLSearchParam because of IE (#2230)
getQueryParam = (name) ->
	match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search)
	if match then decodeURIComponent(match[1].replace(/\+/g, ' ')) else null


# Code from http://snipplr.com/view/29434/
# ----------------------------------------
setSelector = "#appslist"

# Function to write the sorted apps list to session (network errors ignored)
setOrder = ->
	setKey '_appsListOrder', $(setSelector).sortable("toArray").join()

# Function used to remove an OIDC consent
removeOidcConsent = (partner) ->
	#r = new RegExp "\b#{partner}\b,?", 'g'
	#datas['oidcConsents'] = datas['oidcConsents'].replace(r,'').replace(/,$/,'')
	#setKey '_oidcConnectedRP', datas['oidcConsents']
	#	# Success
	#	, () ->
	#		$("[partner='#{partner}']").hide()
	#	# Error
	#	, (j,s,e) ->
	#		alert "#{s} #{e}"
	e = (j,s,e) ->
		alert "#{s} #{e}"
	delKey "_oidcConsents",partner
		# Success
		, () ->
			$("[partner='#{partner}']").hide()
		# Error
		, e

# Function used by setOrder() and removeOidcConsent() to push new values
# For security reason, modification is rejected unless a valid token is given
setKey = (key,val,success,error) ->
	# First request to get token
	$.ajax
		type: "GET"
		url: scriptname + 'mysession/?gettoken'
		dataType: 'json'
		error: error
		# On success, value is set
		success: (data) ->
			d =
				token: data.token
			d[key] = val
			$.ajax
				type: "PUT"
				url: scriptname + 'mysession/persistent'
				dataType: 'json'
				data: d
				success: success
				error: error

delKey = (key,sub,success,error) ->
	$.ajax
		type: "GET"
		url: scriptname + 'mysession/?gettoken'
		dataType: 'json'
		error: error
		# On success, value is set
		success: (data) ->
			$.ajax
				type: "DELETE"
				url: "#{scriptname}mysession/persistent/#{key}?sub=#{sub}&token=#{data.token}"
				dataType: 'json'
				success: success
				error: error

# function that restores the list order from session
restoreOrder = ->
	list = $(setSelector)
	return null unless list? and datas['appslistorder']

	# make array from saved order
	IDs = datas['appslistorder'].split ','

	# fetch current order
	items = list.sortable "toArray"

	# make array from current order
	rebuild = []
	for v in items
		rebuild[v] = v

	for itemID in IDs
		if rebuild[itemID]

			# select item id from current order
			item = rebuild[itemID]

			# select the item according to current order
			child = $(setSelector + ".ui-sortable").children "#" + item

			# select the item according to the saved order
			savedOrd = $(setSelector + ".ui-sortable").children "#" + itemID

			# remove all the items
			child.remove()

			# add the items in turn according to saved order
			# we need to filter here since the "ui-sortable"
			# class is applied to all ul elements and we
			# only want the very first! You can modify this
			# to support multiple lists - not tested!
			$(setSelector + ".ui-sortable").filter(":first").append savedOrd
	1

# function boolean isHiddenFormValueSet(string option)
# Check if an hidden option is set
# @param option Option name
# @return true if option is set, false else
isHiddenFormValueSet = (option) ->
	return $('#lmhidden_' + option).length

# function void ping()
# Check if session is alive on server side
# @return nothing
ping = ->
	$.ajax
		type: "POST"
		url: scriptname
		data:
			ping: 1
		dataType: 'json'
		success: (data) ->
			if data.result? and data.result == 1
				setTimeout ping, datas['pingInterval']
			else
				location.reload true
		error: (j,t,e) ->
			location.reload true

window.ping = ping

# Functions to get/set a cookie value
getCookie = (cname) ->
	name = cname + "="
	ca = decodeURIComponent(document.cookie).split ';'
	re = new RegExp('^ *'+cname+'=')
	for c in ca
		if c.match re
			c = c.replace re, ''
			return c
	return ''

setCookie = (name, value, samesite, exdays) ->
	d = new Date()
	d.setTime d.getTime() + exdays*86400000
	document.cookie = "#{name}=#{value}; expires=#{d.toUTCString()}; path=/; SameSite=#{samesite}"

# Initialization
datas = {}

$(window).on 'load', () ->
	# Get application/init variables
	datas = getValues()

	# Keep the currently selected tab
	if "datas" of window && "choicetab" of window.datas
		datas.choicetab = window.datas.choicetab;

	# Export datas for other scripts
	window.datas = datas

	$("#appslist").sortable
		axis: "y"
		cursor: "move"
		opacity: 0.5
		revert: true
		items: "> div.category"
		update: ->
			setOrder()

	restoreOrder()

	$("div.message").fadeIn 'slow'

	# Set timezone
	$("input[name=timezone]").val -(new Date().getTimezoneOffset() / 60)

	# Menu tabs
	menuTabs = $("#menu").tabs
		active: 0
	menuIndex = $('#menu a[href="#' + datas['displaytab'] + '"]').parent().index()
	menuIndex = 0 if menuIndex < 0
	menuTabs.tabs "option", "active", menuIndex

	# Authentication choice tabs
	authMenuTabs = $("#authMenu").tabs
		active: 0
	authMenuIndex = $('#authMenu a[href="#' + datas['displaytab'] + '"]').parent().index()
	authMenuIndex = 0 if authMenuIndex < 0
	authMenuTabs.tabs "option", "active", authMenuIndex

	# TODO: cookie
	# $("#authMenu").tabs
	# 	cookie:
	# 		name: 'lemonldapauthchoice'
	if datas['choicetab']
		authMenuTabs.tabs "option", "active", $('#authMenu a[href="#' + datas['choicetab'] + '"]').parent().index()

	if datas['login']
		$("input[type=password]:first").focus()
	else
		# If there are no auto-focused fields, focus on first visible input
		if $("input[autofocus]").length == 0
			$("input[type!=hidden]:first").focus();

	# Open links in new windows if required
	if datas['newwindow']
		$('#appslist a').attr "target", "_blank"

	# Complete removeOther link
	if $("p.removeOther").length
		action = $("#form").attr "action"
		method = $("#form").attr "method"
		console.log 'method=', method

		hiddenParams = ""
		if $("#form input[type=hidden]")
			console.log 'Parse hidden values' 
			$("#form input[type=hidden]").each (index) ->
				console.log ' ->', $(this).attr("name"), $(this).val()
				hiddenParams +=  "&" + $(this).attr("name") + "=" + $(this).val()

		back_url = ""
		if action
			console.log 'action=', action
			if action.indexOf("?") != -1
				action.substring(0, action.indexOf("?")) + "?"
			else
				back_url = action + "?"
			back_url += hiddenParams
			hiddenParams = ""

		link = $("p.removeOther a").attr("href") + "&method=" + method + hiddenParams
		link += "&url=" + btoa(back_url) if back_url
		$("p.removeOther a").attr "href", link

	# Language detection. Priority order:
	#  0 - llnglanguage parameter
	#  1 - cookie value
	#  2 - first navigator.languages item that exists in window.availableLanguages
	#  3 - first value of window.availableLanguages
	if window.location.search
		queryLang = getQueryParam('llnglanguage')
		console.log 'Get lang from parameter' if queryLang
		setCookieLang = getQueryParam('setCookieLang')
		console.log 'Set lang cookie' if setCookieLang == 1
	if !lang
		lang = getCookie 'llnglanguage'
		console.log 'Get lang from cookie' if lang && !queryLang
	if !lang
		if navigator
			langs = []
			langs2 = []
			nlangs = [ navigator.language ]
			if navigator.languages
				nlangs = navigator.languages
			for al in window.availableLanguages
				langdiv += "<img class=\"langicon\" src=\"#{window.staticPrefix}common/#{al}.png\" title=\"#{al}\" alt=\"[#{al}]\"> "
			for nl in nlangs
				console.log 'Navigator lang', nl
				for al in window.availableLanguages
					console.log ' Available lang', al
					re = new RegExp('^'+al+'-?')
					if nl.match re
						console.log '  Matching lang =', al
						langs.push al
					else if al.substring(0, 1) == nl.substring(0, 1)
						langs2.push al
			lang = if langs[0] then langs[0] else if langs2[0] then langs2[0] else window.availableLanguages[0]
			console.log 'Get lang from navigator' if lang && !queryLang
		else
			lang = window.availableLanguages[0]
			console.log 'Get lang from window' if lang && !queryLang
	else if lang not in window.availableLanguages
		lang = window.availableLanguages[0]
		console.log 'Lang not available -> Get default lang' if !queryLang
	if queryLang
		if queryLang not in window.availableLanguages
			console.log 'Lang not available -> Get default lang'
			queryLang = window.availableLanguages[0]
		console.log 'Selected lang ->', queryLang
		if setCookieLang
			console.log 'Set cookie lang ->', queryLang
			setCookie 'llnglanguage', queryLang, datas['sameSite']
		translatePage(queryLang)
	else
		console.log 'Selected lang ->', lang
		setCookie 'llnglanguage', lang, datas['sameSite']
		translatePage(lang)

	# Build language icons
	langdiv = ''
	for al in window.availableLanguages
		langdiv += "<img class=\"langicon\" src=\"#{window.staticPrefix}common/#{al}.png\" title=\"#{al}\" alt=\"[#{al}]\"> "
	$('#languages').html langdiv
	$('.langicon').on 'click', () ->
		lang = $(this).attr 'title'
		setCookie 'llnglanguage', lang, datas['sameSite']
		translatePage lang

	isAlphaNumeric = (chr) ->
		code = chr.charCodeAt(0)
		if code > 47 and code < 58 or code > 64 and code < 91 or code > 96 and code < 123
			return true
		false

	# Password policy
	checkpassword = (password) ->
		result = true
		if window.datas.ppolicy.minsize > 0
			result = false if setDanger( password.length >= window.datas.ppolicy.minsize, 'ppolicy-minsize-feedback' )
		if window.datas.ppolicy.minupper > 0
			upper = password.match(/[A-Z]/g)
			result = false if setDanger( upper and upper.length >= window.datas.ppolicy.minupper, 'ppolicy-minupper-feedback' )
		if window.datas.ppolicy.minlower > 0
			lower = password.match(/[a-z]/g)
			result = false if setDanger( lower and lower.length >= window.datas.ppolicy.minlower, 'ppolicy-minlower-feedback')
		if window.datas.ppolicy.mindigit > 0
			digit = password.match(/[0-9]/g)
			result = false if setDanger( digit and digit.length >= window.datas.ppolicy.mindigit, 'ppolicy-mindigit-feedback')

		if window.datas.ppolicy.allowedspechar
			nonwhitespechar = window.datas.ppolicy.allowedspechar.replace(/\s/g, '')
			hasforbidden = false
			i = 0
			len = password.length
			while i < len
				if !isAlphaNumeric(password.charAt(i))
					if nonwhitespechar.indexOf(password.charAt(i)) < 0
						hasforbidden = true
				i++
			result = false if setDanger( hasforbidden == false, 'ppolicy-allowedspechar-feedback' )

		if window.datas.ppolicy.minspechar > 0 and window.datas.ppolicy.allowedspechar
			numspechar = 0
			nonwhitespechar = window.datas.ppolicy.allowedspechar.replace(/\s/g, '')
			i = 0
			while i < password.length
				if nonwhitespechar.indexOf(password.charAt(i)) >= 0
					numspechar++
				i++
			result = false if setDanger( numspechar >= window.datas.ppolicy.minspechar, 'ppolicy-minspechar-feedback')

		if window.datas.ppolicy.minspechar > 0 and !window.datas.ppolicy.allowedspechar
			numspechar = 0
			i = 0
			while i < password.length
				numspechar++ if !isAlphaNumeric(password.charAt(i))
				i++
			result = false if setDanger( numspechar >= window.datas.ppolicy.minspechar, 'ppolicy-minspechar-feedback')

		if result
			$('.ppolicy').removeClass('border-danger').addClass 'border-success'
			$('#newpassword').get(0)?.setCustomValidity('')
		else
			$('.ppolicy').removeClass('border-success').addClass 'border-danger'
			$('#newpassword').get(0)?.setCustomValidity(translate('PE28'))
		return

	if window.datas.ppolicy? and $('#newpassword').length
		# Initialize display
		checkpassword ''

		$('#newpassword').keyup (e) ->
			checkpassword e.target.value
			return

	# If generating password, disable policy check
	togglecheckpassword = (e) ->
		if e.target.checked
			$('#newpassword').off('keyup')
			$('#newpassword').get(0)?.setCustomValidity('')
		# Restore check
		else
			$('#newpassword').keyup (e) ->
				checkpassword e.target.value
				return
			checkpassword ''

	checksamepass = () ->
		if $('#confirmpassword').get(0)?.value == $('#newpassword').get(0)?.value
			$('#confirmpassword').get(0)?.setCustomValidity('')
			return true
		else
			$('#confirmpassword').get(0)?.setCustomValidity(translate('PE34'))
			return false

	$('#newpassword').change checksamepass
	$('#confirmpassword').change checksamepass
	if window.datas.ppolicy? and $('#newpassword').length
		$('#reset').change togglecheckpassword

	# Set local dates (used to display history)
	$(".localeDate").each ->
		s = new Date($(this).attr("val")*1000)
		$(this).text s.toLocaleString()

	$('.oidcConsent').on 'click', () ->
		removeOidcConsent $(this).attr 'partner'

	# Ping if asked
	if datas['pingInterval'] and datas['pingInterval'] > 0
		window.setTimeout ping, datas['pingInterval']

	# Functions to show/hide display password button
	if datas['enablePasswordDisplay']
		field = ''
		if datas['dontStorePassword']
			$(".toggle-password").on 'mousedown touchstart', () ->
				field = $(this).attr 'id'
				field = field.replace /^toggle_/, ''
				console.log 'Display', field
				$(this).toggleClass("fa-eye fa-eye-slash")
				$("input[name=#{field}]").attr('class', 'form-control')
			$(".toggle-password").on 'mouseup touchend', () ->
				$(this).toggleClass("fa-eye fa-eye-slash")
				$("input[name=#{field}]").attr('class', 'form-control key') if $("input[name=#{field}]").get(0).value
		else
			$(".toggle-password").on 'mousedown touchstart', () ->
				field = $(this).attr 'id'
				field = field.replace /^toggle_/, ''
				console.log 'Display', field
				$(this).toggleClass("fa-eye fa-eye-slash")
				$("input[name=#{field}]").attr("type", "text")
			$(".toggle-password").on 'mouseup touchend', () ->
				$(this).toggleClass("fa-eye fa-eye-slash")
				$("input[name=#{field}]").attr("type", "password")

	# Functions to show/hide newpassword inputs
	$('#reset').change () ->
		checked = $(this).prop('checked')
		console.log 'Reset is checked', checked
		if checked == true
			$('#newpasswords').hide()
			$('#newpassword').removeAttr('required')
			$('#confirmpassword').removeAttr('required')
			$('#confirmpassword').get(0)?.setCustomValidity('')
		else
			$('#newpasswords').show()
			$('#newpassword').attr('required', true)
			$('#confirmpassword').attr('required', true)
			if $('#confirmpassword').get(0)?.value == $('#newpassword').get(0)?.value
				$('#confirmpassword').get(0)?.setCustomValidity('')
			else
				$('#confirmpassword').get(0)?.setCustomValidity(translate('PE34'))

	# Functions to show/hide placeholder password inputs
	$('#passwordfield').on 'input', () ->
		if $('#passwordfield').get(0).value && datas['dontStorePassword']
			$("#passwordfield").attr('class', 'form-control key')
		else
			$("#passwordfield").attr('class', 'form-control')
	$('#oldpassword').on 'input', () ->
		if $('#oldpassword').get(0).value && datas['dontStorePassword']
			$("#oldpassword").attr('class', 'form-control key')
		else
			$("#oldpassword").attr('class', 'form-control')
	$('#newpassword').on 'input', () ->
		if $('#newpassword').get(0).value && datas['dontStorePassword']
			$("#newpassword").attr('class', 'form-control key')
		else
			$("#newpassword").attr('class', 'form-control')
	$('#confirmpassword').on 'input', () ->
		if $('#confirmpassword').get(0).value && datas['dontStorePassword']
			$("#confirmpassword").attr('class', 'form-control key')
		else
			$("#confirmpassword").attr('class', 'form-control')

	#$('#formpass').on 'submit', changePwd

	$('.clear-finduser-field').on 'click', () ->
		$(this).parent().find(':input').each ->
			console.log 'Clear search field ->', $(this).attr 'name'
			$(this).val ''

	$('#closefinduserform').on 'click', () ->
		console.log 'Clear modal'
		$('#finduserForm').trigger('reset')

	$('#finduserbutton').on 'click', (event) ->
		event.preventDefault()
		document.body.style.cursor = 'progress'
		str = $("#finduserForm").serialize()
		console.log 'Send findUser request with parameters', str
		$.ajax
			type: "POST"
			url: "#{portal}finduser"
			dataType: 'json'
			data: str
			# On success, values are set
			success: (data) ->
				document.body.style.cursor = 'default'
				user = data.user
				console.log 'Suggested spoofId=', user
				$("input[name=spoofId]").each ->
					$(this).val user
				$('#captcha').attr 'src', data.captcha if data.captcha
				if data.token
					$('#finduserToken').val data.token 
					$('#token').val data.token
			error: (j, status, err) ->
				document.body.style.cursor = 'default'
				console.log 'Error', err  if err
				res = JSON.parse j.responseText if j
				if res and res.error
					console.log 'Returned error', res

	$('#btn-back-to-top').on 'click', () ->
		console.log 'Back to top'
		document.body.scrollTop = 0
		document.documentElement.scrollTop = 0

	$(window).on 'scroll', () ->
		if datas['scrollTop'] && (document.body.scrollTop > Math.abs(datas['scrollTop']) || document.documentElement.scrollTop > Math.abs(datas['scrollTop']))
			$('#btn-back-to-top').css("display","block")
		else
			$('#btn-back-to-top').css("display","none")

	$(document).trigger "portalLoaded"
	true
