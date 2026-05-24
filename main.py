import sqlite3
import aggregator
import backend

if __name__ == "__main__":
    conn = backend.get_connection()
    backend.init_db(conn)

    matches = aggregator.agg_jobs()
    backend.save_jobs(conn, matches)

    conn.close()