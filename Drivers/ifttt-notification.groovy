metadata {
  definition (name: "Virtual Notification Device"", namespace: "RMoRobert", author: "Robert Morris") {
    capability "Notification"
  }

  preferences {
    input("postUrl", "text", title: "Post Url:", description: "The Url to post to")
    input(name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: true)
    input(name: "txtEnable", type: "bool", title: "Enable descriptionText logging", defaultValue: true)
  }
}

void installed() {
  log.debug "installed()"
  initialize()
}

void updated() {
  log.debug "updated()"
  initialize()
}

void initialize() {
  log.debug "initialize()"
  Integer disableTime = 1800
  if (logEnable) {
    log.debug "Debug logging will be automatically disabled in ${disableTime} seconds"
    runIn(disableTime, "debugOff")
  }
}

void debugOff() {
  log.warn "Disabling debug logging"
  device.updateSetting("logEnable", [value:"false", type:"bool"])
}

void deviceNotification(notificationText) {

  Map params = [
      uri:  "${postUrl}",
      contentType: "application/json",
      body: [value1: notificationText],  // this will get converted to a JSON
      timeout: 15
  ]
  asynchttpPost("myAsynchttpHandler", params)
}

void myAsynchttpHandler(resp, data) {
  if (logEnable) log.debug "HTTP ${resp.status}"
  // whatever you might need to do here (check for errors, etc.),
  if (logEnable) log.debug "HTTP ${resp.body}"
}