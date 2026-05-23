import sqlite3
import feedparser
import aggregator

DB_PATH = "nexus-backend/jobs.db"

def save_to_database(matches):
    conn = sqlite3.connect(DB_PATH)
    cursor = conn.cursor()

    cursor.execute('''
        CREATE TABLE IF NOT EXISTS jobs (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT NOT NULL,
            description TEXT NOT NULL,
            source TEXT,
            published TEXT,
            url TEXT UNIQUE,
            vector_id TEXT
        )
    ''')

    cursor.execute('''
        CREATE TABLE IF NOT EXISTS resumes (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id INTEGER NOT NULL,
            raw_text TEXT NOT NULL,
            vector_id TEXT,
            uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP
        )
    ''')

    if matches:
        cursor.executemany(
            """
            INSERT OR IGNORE INTO jobs (title, description, source, published, url)
            VALUES (:title, :description, :source, :published, :url)
            """,
            matches
        )
        print(f"Inserted {cursor.rowcount} rows.")

    conn.commit()

    conn.close()

    print("Table created successfully.")