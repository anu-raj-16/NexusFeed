import aggregator
import backend

jobs_list = aggregator.agg_jobs()

backend.save_to_database(jobs_list)