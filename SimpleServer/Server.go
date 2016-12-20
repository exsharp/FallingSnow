package main

import (
	"bufio"
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
	"strings"
)

type info struct {
	num string
	det string
}

var data []info
var filePath = "./db.txt"
var logFile = "./log.txt"
var logHdl *log.Logger

func readDB(path string) ([]info, error) {
	file, err := os.Open(path)
	if err != nil {
		return nil, err
	}
	defer file.Close()

	var lines []info

	buf := bufio.NewReader(file)
	for {
		line, err := buf.ReadString('\n')
		line = strings.TrimSpace(line)
		//println(line)
		tmp := strings.SplitN(line, " ", 2)
		if len(tmp) > 1 {
			lines = append(lines, info{tmp[0], tmp[1]})
		}
		if err != nil {
			if err == io.EOF {
				return lines, nil
			}
			return nil, err
		}
	}
}

func writeDB(path string, data []info) error {
	file, err := os.Create(path)
	if err != nil {
		return err
	}
	defer file.Close()
	for _, l := range data {
		line := l.num + " " + l.det + "\n"
		if _, err := file.WriteString(line); err != nil {
			return err
		}
	}
	return nil
}

func addInfo(num string, text string) {
	data = append(data, info{num, text})
}

func getGreetings(num string) string {
	ret := ""
	for _, i := range data {
		if i.num == num {
			ret += (i.det + "\n")
		}
	}
	return ret
}

func postInfo(w http.ResponseWriter, req *http.Request) {
	req.ParseForm()
	value1 := req.PostFormValue("Number")
	value2 := req.PostFormValue("Content")
	if len(value1) > 0 && len(value2) > 0 {
		addInfo(value1, value2)
		logHdl.Printf("[%s  ->  %s]", value1, value2)
		fmt.Println(value1 + " " + value2)
	}
	io.WriteString(w, "OK")
}

func getInfo(w http.ResponseWriter, req *http.Request) {
	req.ParseForm()
	tag := "Number"
	value := ""
	if len(req.Form[tag]) > 0 {
		value = req.Form[tag][0]
		logHdl.Printf("%s来访", value)
		fmt.Println(value)
	}
	ret := getGreetings(value)
	io.WriteString(w, ret)
}

func main() {
	logFile, err := os.Create(logFile)
	defer logFile.Close()
	if err != nil {
		log.Fatalln("open file error !")
	}
	logHdl = log.New(logFile, "[Debug]", log.Llongfile)

	data, err = readDB(filePath)
	if err != nil {
		println(err)
	}
	http.HandleFunc("/getInfo", getInfo)
	http.HandleFunc("/postInfo", postInfo)
	//err := http.ListenAndServe(":80", http.FileServer(http.Dir("./")))
	err = http.ListenAndServe(":8012", nil)
	if err != nil {
		log.Fatal("ListenAndServe: ", err)
	}
	//为什么不能做文件服务器
}
