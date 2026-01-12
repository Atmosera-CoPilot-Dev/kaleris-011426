package main

import (
	"fmt"
	"net/http"
	"time"
)

// Simulated database
var database = map[string]map[string]string{
	"1": {
		"basicInfo":   "Name: John Doe, Age: 30",
		"address":     "123 Main St, Springfield",
		"preferences": "Likes: Coffee, Music",
	},
}

// getUserBasicInfo simulates a database query for basic user information.
func getUserBasicInfo(userID string) string {
	time.Sleep(100 * time.Millisecond) // Simulate slow database query
	return database[userID]["basicInfo"]
}

// getUserAddress simulates a database query for user address.
func getUserAddress(userID string) string {
	time.Sleep(100 * time.Millisecond) // Simulate slow database query
	return database[userID]["address"]
}

// getUserPreferences simulates a database query for user preferences.
func getUserPreferences(userID string) string {
	time.Sleep(100 * time.Millisecond) // Simulate slow database query
	return database[userID]["preferences"]
}

// getUserProfile handles the `/getUserProfile` endpoint.
func getUserProfile(w http.ResponseWriter, r *http.Request) {
	userID := r.URL.Query().Get("userID")
	if userID == "" {
		http.Error(w, "userID is required", http.StatusBadRequest)
		return
	}

	// Inefficiently fetch data in multiple queries
	basicInfo := getUserBasicInfo(userID)
	address := getUserAddress(userID)
	preferences := getUserPreferences(userID)

	// Inefficient string concatenation
	response := ""
	response += "{"
	response += "\"basicInfo\": \"" + basicInfo + "\","
	response += "\"address\": \"" + address + "\","
	response += "\"preferences\": \"" + preferences + "\""
	response += "}"

	// Send the response
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	_, _ = w.Write([]byte(response))
}

func main() {
	http.HandleFunc("/getUserProfile", getUserProfile)

	fmt.Println("Starting server on :8080...")
	if err := http.ListenAndServe(":8080", nil); err != nil {
		fmt.Println("Server failed:", err)
	}
}
