package main

import (
	"database/sql"
	"fmt"
	"log"
	"net/http"

	_ "github.com/go-sql-driver/mysql"
)

func main() {
	db, err := sql.Open("mysql", "user:123456@tcp(8.139.5.39:3306)/test")
	if err != nil {
		log.Fatal(err)
	}
	defer db.Close()

	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "text/html")

		rows, err := db.Query("SELECT * FROM students")
		if err != nil {
			log.Fatal(err)
		}
		defer rows.Close()

		fmt.Fprint(w, `<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8"/>
</head>
<div>Here is Go
</div>
<table border="1">
<tr>
<th>学号</th>
<th>姓名</th>
</tr>`)

		for rows.Next() {
			var stuId, stuName string
			err := rows.Scan(&stuId, &stuName)
			if err != nil {
				log.Fatal(err)
			}
			fmt.Fprintf(w, "<tr><td>%s</td><td>%s</td></tr>", stuId, stuName)
		}

		fmt.Fprint(w, "</table></html>")
	})

	log.Fatal(http.ListenAndServe(":8800", nil))
}

