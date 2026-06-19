import psycopg2
import os
import aggregator

def get_connection():
    return psycopg2.connect(os.getenv("PG_URL"))


def init_db(conn):
    cursor = conn.cursor()

    cursor.execute('''
        CREATE TABLE IF NOT EXISTS jobs (
            id SERIAL PRIMARY KEY,
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
            id SERIAL PRIMARY KEY,
            user_id INTEGER NOT NULL,
            raw_text TEXT NOT NULL,
            vector_id TEXT,
            uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
        INSERT INTO jobs (title, description, source, published, url)
        VALUES (%(title)s, %(description)s, %(source)s, %(published)s, %(url)s)
        ON CONFLICT (url) DO NOTHING
        """,
        matches
    )
    conn.commit()
    print(f"Inserted {cursor.rowcount} new jobs.")
