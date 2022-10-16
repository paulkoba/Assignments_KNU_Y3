package main

import (
	"fmt"
	"math/rand"
	"time"
)

func smoker(uniqueComponent string, forward chan bool, back chan bool) {
	for {
		<- forward
		fmt.Println("Smoker who has", uniqueComponent, "is smoking")
		time.Sleep(time.Second)
		back <- true
	}
}

func main() {
	var tobaccoChannel = make(chan bool, 1)
	var paperChannel = make(chan bool, 1)
	var matchesChannel = make(chan bool, 1)
	var smokingChannel = make(chan bool, 1)

	go smoker("tobacco", tobaccoChannel, smokingChannel)
	go smoker("paper", paperChannel, smokingChannel)
	go smoker("matches", matchesChannel, smokingChannel)

	for {
		var idx = rand.Int() % 3
		switch idx {
		case 0:
			tobaccoChannel <- true
		case 1:
			paperChannel <- true
		case 2:
			matchesChannel <- true
		default:
			fmt.Println("Unreachable")
		}
		<- smokingChannel
	}
}