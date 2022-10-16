package main

import "fmt"

func standoff(powerLevels []int, left int, right int) int {
	fmt.Print(left + 1, " and ", right + 1, " fighting. ")

	if powerLevels[left] > powerLevels[right] {
		fmt.Println(left + 1, "won")
		return left
	} else {
		fmt.Println(right + 1, "won")
		return right
	}
}

// Modified mergesort, kinda
func fight(powerLevels []int, left int, right int, winners chan int) {
	// Shouldn't ever happend
	if right < left {
		return
	}

	// Didn't fight, trivial winner
	if right == left {
		winners <- left
		return
	}

	// Last iteration, 2 competitors, winner logic can be modified as it is not really defined in task
	if right == left + 1 {
		winners <- standoff(powerLevels, left, right)
		return
	}

	// Default case

	var mid = (left + right) / 2

	var subChannel = make(chan int, 2)
	go fight(powerLevels, left, mid, subChannel)
	go fight(powerLevels, mid + 1, right, subChannel)

	var leftWinner = <- subChannel
	var rightWinner = <- subChannel

	winners <- standoff(powerLevels, leftWinner, rightWinner)
}

func main() {
	var winner = make(chan int, 1)
	var powerLevels = []int{ 1, 5, 3, 2, 4, 7, 10, 8, 9, 6 }

	fight(powerLevels, 0, len(powerLevels) - 1, winner)

	var won = <- winner

	fmt.Println("Won:", won + 1)

}
