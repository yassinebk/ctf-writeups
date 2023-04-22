package main

import (
	"bufio"
	"bytes"
	"context"
	"crypto/sha256"
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"io/ioutil"
	"math/rand"
	"net/http"
	"os"
	"time"
)

// The password here is 32 bytes.
var password = sha256.Sum256([]byte("idek"))
var randomData []byte

const (
	MaxOrders = 10
)

func initRandomData() {
	rand.Seed(1337)
	randomData = make([]byte, 24576)
	if _, err := rand.Read(randomData); err != nil {
		panic(err)
	}
	copy(randomData[12625:], password[:])

}

type ReadOrderReq struct {
	Orders []int `json:"orders"`
}

func justReadIt(w http.ResponseWriter, r *http.Request) {
	fmt.Println()
	fmt.Println()
	fmt.Println()
	defer r.Body.Close()

	body, err := ioutil.ReadAll(r.Body)
	if err != nil {
		w.WriteHeader(500)
		w.Write([]byte("bad request\n"))
		return
	}

	reqData := ReadOrderReq{}
	if err := json.Unmarshal(body, &reqData); err != nil {
		w.WriteHeader(500)
		w.Write([]byte("invalid body\n"))
		return
	}

	if len(reqData.Orders) > MaxOrders {
		w.WriteHeader(500)
		w.Write([]byte("whoa there, max 10 orders!\n"))
		return
	}

	// Reading from the random Data
	reader := bytes.NewReader(randomData)

	// buf := make([]byte, 24576)
	// reader.Read(buf)

	// fmt.Println("Testing to read from the reader", buf)

	validator := NewValidator()

	// Creating a context here that we will use
	ctx := context.Background()
	for _, o := range reqData.Orders {
		if err := validator.CheckReadOrder(o); err != nil {
			w.WriteHeader(500)
			w.Write([]byte(fmt.Sprintf("error: %v\n", err)))
			return
		}

		// Context with validator, attaching a reader with a fixed size
		ctx = WithValidatorCtx(ctx, reader, int(o))

		// Read from the context and get the random bytes from the long bytes thing
		// Here lays the problem. We are reading from the ctx and returning the next buffer as ctx
		// old_ctx := ctx
		ctx, err := validator.Read(ctx) // We are returning the buffer we have read as a ctx

		// comparing old and new ctx
		// fmt.Println("My context old\t", old_ctx)
		fmt.Println("My context new\t", ctx)

		if err != nil {
			w.WriteHeader(500)
			w.Write([]byte(fmt.Sprintf("failed to read: %v\n", err)))
			return
		}
	}

	if err := validator.Validate(ctx); err != nil {
		w.WriteHeader(500)
		w.Write([]byte(fmt.Sprintf("validation failed: %v\n", err)))
		return
	}

	println("Passed to here, context validated")
	w.WriteHeader(200)
	w.Write([]byte(os.Getenv("FLAG")))
}

func main() {
	if _, exists := os.LookupEnv("LISTEN_ADDR"); !exists {
		panic("env LISTEN_ADDR is required")
	}
	if _, exists := os.LookupEnv("FLAG"); !exists {
		panic("env FLAG is required")
	}

	initRandomData()

	http.HandleFunc("/just-read-it", justReadIt)

	srv := http.Server{
		Addr:         os.Getenv("LISTEN_ADDR"),
		ReadTimeout:  5 * time.Second,
		WriteTimeout: 5 * time.Second,
	}

	fmt.Printf("Server listening on %s\n", os.Getenv("LISTEN_ADDR"))
	if err := srv.ListenAndServe(); err != nil {
		panic(err)
	}

}

type Validator struct{}

func NewValidator() *Validator {
	return &Validator{}
}

func (v *Validator) CheckReadOrder(o int) error {
	if o <= 0 || o > 100 {
		return fmt.Errorf("invalid order %v", o)
	}
	return nil
}

func (v *Validator) Read(ctx context.Context) ([]byte, error) {
	// The reader is the password and the size is passed down too
	r, s := GetValidatorCtxData(ctx)
	buf := make([]byte, s)
	// fmt.Print("Initialized buffer", buf)
	// I read the buffer here
	_, err := r.Read(buf)

	fmt.Println("The current index of the data is ", bytes.Index(randomData, buf))

	fmt.Print()
	if err != nil {
		return nil, fmt.Errorf("read error: %v", err)
	}
	return buf, nil
}

func (v *Validator) Validate(ctx context.Context) error {
	r, _ := GetValidatorCtxData(ctx)
	// Read 32 of the random data
	buf, err := v.Read(WithValidatorCtx(ctx, r, 32))

	if err != nil {
		return err
	}
	// fmt.Println("BUffer value",buf)
	fmt.Println("Buffer Value", buf)
	fmt.Println("Password Value", password)

	if bytes.Compare(buf, password[:]) != 0 {
		return errors.New("invalid password")
	}
	return nil
}

const (
	reqValReaderKey = "readerKey"
	reqValSizeKey   = "reqValSize"
)

func GetValidatorCtxData(ctx context.Context) (io.Reader, int) {
	reader := ctx.Value(reqValReaderKey).(io.Reader)
	size := ctx.Value(reqValSizeKey).(int)
	if size >= 100 {
		reader = bufio.NewReader(reader)
	}

	fmt.Println("Current size", size, " Position of the reader ")
	return reader, size
}

func WithValidatorCtx(ctx context.Context, r io.Reader, size int) context.Context {
	ctx = context.WithValue(ctx, reqValReaderKey, r)
	ctx = context.WithValue(ctx, reqValSizeKey, size)

	return ctx
}
