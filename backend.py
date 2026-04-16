import sqlite3
import feedparser
import aggregator

def save_to_database(matches):
    conn = sqlite3.connect("jobs.db")
    cursor = conn.cursor()

    cursor.execute('''
        CREATE TABLE IF NOT EXISTS jobs (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT NOT NULL,
            description TEXT NOT NULL,
            source TEXT,
            published TEXT
        )
    ''')

    if matches:
        cursor.executemany(
            "INSERT INTO jobs (title, description, source, published) VALUES (:title, :description, :source, :published)", 
            matches
        )
        print(f"Inserted {cursor.rowcount} rows.")

    conn.commit()

    conn.close()

    print("Table created successfully.")