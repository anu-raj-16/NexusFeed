import sqlite3
import aggregator

DB_PATH = "nexus-backend/jobs.db"


def get_connection():
    return sqlite3.connect(DB_PATH)


def init_db(conn):
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

    conn.commit()
    print("Database initialized.")


def save_jobs(conn, matches):
    if not matches:
        print("No matches to insert.")
        return

    cursor = conn.cursor()
    cursor.executemany(
        """
        INSERT OR IGNORE INTO jobs (title, description, source, published, url)
        VALUES (:title, :description, :source, :published, :url)
        """,
        matches
    )
    conn.commit()
    print(f"Inserted {cursor.rowcount} new jobs.")
