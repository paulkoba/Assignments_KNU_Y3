package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

type OutgoingRoute struct {
	dest int
	price int
}

type Graph struct {
	lock sync.RWMutex
	edges map[int][]*OutgoingRoute
}

func (graph *Graph) addEdge(from int, to int, price int) {
	graph.lock.Lock();

	if(graph.edges == nil) {
		graph.edges = make(map[int][]*OutgoingRoute)
	}

	e1 := OutgoingRoute { dest: to, price: price }
	e2 := OutgoingRoute { dest: from, price: price }

	graph.edges[from] = append(graph.edges[from], &e1)
	graph.edges[to] = append(graph.edges[to], &e2)

	graph.lock.Unlock();
}

// Returns price of direct route or -1 if the route doesn't exist
func (graph *Graph) getPathPrice(from int, to int) int {
	if(graph.edges == nil) {
		return -1
	}

	graph.lock.Lock();

	for i := 0; i < len(graph.edges[from]); i++ {
		if(graph.edges[from][i].dest == to) {
			graph.lock.Unlock();
			return graph.edges[from][i].price
		}
	}

	graph.lock.Unlock();

	return -1
}

func (graph *Graph) removePathInternal(from int, to int) {
	if(graph.edges == nil) {
		return
	}

	for i := 0; i < len(graph.edges[from]); i++ {
		if(graph.edges[from][i].dest == to) {
			graph.edges[from] = append(graph.edges[from][:i], graph.edges[from][i+1:]...)
			return
		}
	}
}

func (graph *Graph) removePath(from int, to int) {
	graph.lock.Lock()

	graph.removePathInternal(from, to)
	graph.removePathInternal(to, from)

	graph.lock.Unlock()
}

func (graph *Graph) updatePathPrice(from int, to int, price int) {
	graph.removePath(from, to)
	graph.addEdge(from, to, price)
}

func (graph *Graph) deleteCity(i int) {
	graph.lock.Lock()

	for j := 0; j < len(graph.edges[i]); j++ {
		graph.removePathInternal(graph.edges[i][j].dest, i)
	}
	
	graph.edges[i] = nil

	graph.lock.Unlock()
}

func pathfinder(graph *Graph) {
	for {
		var x = rand.Intn(10)
		var y = rand.Intn(10)
		var price = graph.getPathPrice(x, y)
		if(price != -1) {
			fmt.Print("Path between (", x, ", ", y, ") exists and costs ", price, ".\n")
		}
		time.Sleep(time.Second)
	}
}

func cityDeleter(graph *Graph) {
	for {
		time.Sleep(time.Second * 10)
		var i = rand.Intn(10)
		graph.deleteCity(i)
		fmt.Print("Deleted city ", i, ".\n")
	}
}

func routeCreator(graph *Graph) {
	for {
		time.Sleep(time.Millisecond * 250)
		var x = rand.Intn(10)
		var y = rand.Intn(10)
		var price = rand.Intn(10) + 1
		graph.addEdge(x, y, price)
		fmt.Print("Added edge (", x, y, ") with price ", price,"\n")
	}
}

func priceUpdater(graph *Graph) {
	for {
		time.Sleep(time.Second)
		var x = rand.Intn(10)
		var y = rand.Intn(10)
		var price = graph.getPathPrice(x, y)
		if(price != -1) {
			graph.updatePathPrice(x, y, price + 1)
			fmt.Print("Path between (", x, ", ", y, ") exists and its cost was updated to ", price + 1, ".\n")
		}
	}
}

func main() {
	var graph = Graph{}

	for i:= 0; i < 20; i++ {
		var x = rand.Intn(10)
		var y = rand.Intn(10)
		var price = rand.Intn(10) + 1
		graph.addEdge(x, y, price)
		fmt.Print("Added edge (", x, y, ") with price ", price,"\n")
	}

	for i:= 0; i < 10; i++ {
		var x = rand.Intn(10)
		var y = rand.Intn(10)
		var price = graph.getPathPrice(x, y)
		if(price != -1) {
			graph.removePath(x, y)
		}
	}
	go routeCreator(&graph)
	go cityDeleter(&graph)
	go pathfinder(&graph)
	go priceUpdater(&graph)

	for {}
}