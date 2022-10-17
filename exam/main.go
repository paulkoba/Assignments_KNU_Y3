package main

import (
	"fmt"
	"math/rand"
	"time"
)

type registry struct {
	value int
	idx int
	messageChannel chan message
	backChannel chan bool
	controller chan int
}

// Placeholder, more fields can be added in future
type account struct {
	value int
}

type message struct {
	messageType int
	value int
	acc *account
}

func cashier(registries *[]registry, idx int) {
	for {
		var mes = <- (*registries)[idx].messageChannel

		if(mes.messageType != 1 && mes.messageType != 2) {
			fmt.Println("Unreachable")
			return
		}

		if(mes.messageType == 1) {
			if((*registries)[idx].value < mes.value) {
				fmt.Println("Not enough money in registry")
				(*registries)[idx].controller <- idx

				continue
			}

			(*registries)[idx].value -= mes.value
			(*mes.acc).value -= mes.value

			if((*registries)[idx].value < 15) {
				(*registries)[idx].controller <- (*registries)[idx].idx
			}

		} else {
			if((*registries)[idx].value + mes.value > 100) {
				fmt.Println("Not enough space in registry")
				(*registries)[idx].controller <- idx
				continue
			}

			(*registries)[idx].value += mes.value
			(*mes.acc).value += mes.value

			if((*registries)[idx].value > 85) {
				(*registries)[idx].controller <- (*registries)[idx].idx
			}
		}
	}
}

func controller(registries *[]registry, ch chan int) {
	for {
		var toService = <- ch
		fmt.Println("Servicing registry", (*registries)[toService].idx, "which had", (*registries)[toService].value, "dollars in it")

		(*registries)[toService].value = 50
	}
}

func customer(acc *account, channel chan message) {
	if(rand.Intn(2) == 1) {
		channel <- message{1, rand.Intn(10), acc } // Take out N dollars
	} else {
		channel <- message{2, rand.Intn(10), acc } // Add N dollars
	}
}

func main() {
	fmt.Println("Setting up...")

	var controllerChannel = make(chan int, 1)
	var registries []registry
	
	for i := 0; i < 10; i++ {
		var tmpRegistry registry
		tmpRegistry.value = 50
		tmpRegistry.messageChannel = make(chan message)
		tmpRegistry.controller = controllerChannel
		tmpRegistry.idx = i
		registries = append(registries, tmpRegistry)

		go cashier(&registries, i)
	}

	go controller(&registries, controllerChannel)

	for {
		var chosen = rand.Intn(10);
		var acc = account{50}
		customer(&acc, registries[chosen].messageChannel)
		
		time.Sleep(10 * time.Millisecond)
	}
}