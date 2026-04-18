import aggregator
import backend
import sqlite3

conn = sqlite3.connect("nexus-backend/jobs.db")
cursor = conn.cursor()
cursor.execute("DROP TABLE IF EXISTS jobs;")
conn.commit()


jobs_list = aggregator.agg_jobs()

backend.save_to_database(jobs_list)