package main

import (
	"bufio"
	"fmt"
	"io/ioutil"
	"net/http"
	"net/url"
	"os"
	"strings"
)

func post(num string, text string) {
	resp, err := http.PostForm("http://5tangs.com:8012/postInfo",
		url.Values{"Number": {num}, "Content": {text}})

	if err != nil {
		// handle error
	}

	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		// handle error
	}

	fmt.Println(string(body))
}

func inputM() (string, string) {
	inputReader := bufio.NewReader(os.Stdin)
	println("输入电话号码，内容，空格分开: ")
	input, err := inputReader.ReadString('\n')
	if err != nil {
		println("输入格式错误")
	}
	input = strings.Trim(input, "\n")
	inputs := strings.Split(input, " ")
	if len(inputs) < 2 {
		println("输入错误")
	}
	number := inputs[0]
	text := inputs[1]
	println("输入内容: " + number + " -> " + text + "\n")
	return number, text
}

func main() {
	//post("18814098702", "Happy")
	for {
		n, t := inputM()
		post(n, t)
	}
}
