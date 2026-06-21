# NexusFeed

An AI-powered job aggregator and resume matcher. NexusFeed scrapes remote job listings, lets users upload their resume, and uses Google's Gemini API to score how well their resume matches each job — highlighting matched skills and gaps for every listing.

**Live demo:** https://anu-raj-16.github.io/NexusFeed/

## How It Works

1. A Python scraper pulls remote job listings from We Work Remotely on a daily schedule (via GitHub Actions) and stores them in a shared PostgreSQL database.
2. A Spring Boot REST API serves job listings and handles resume uploads.
3. When a user uploads a resume (PDF), the backend extracts the text using Apache PDFBox and stores it.
4. On request, the backend sends the resume text and each job description to Gemini, which returns a match score (0–100), matched skills, and skill gaps.
5. Results are ranked by score and returned to a simple web frontend.

## Tech Stack

| Layer | Technology |
|---|---|
| Job scraper | Python, feedparser, BeautifulSoup |
| Scheduler | GitHub Actions (daily cron) |
| Backend API | Java 21, Spring Boot 4, Spring Data JPA |
| Database | PostgreSQL (hosted on Render) |
| AI matching | Google Gemini API |
| PDF parsing | Apache PDFBox |
| Frontend | Vanilla HTML/CSS/JavaScript |
| Hosting | Render |
| Testing | JUnit 5, Mockito, MockMvc |

## Architecture

```
┌─────────────────┐
│  GitHub Actions  │  (runs daily)
│  Python Scraper  │
└────────┬─────────┘
         │ writes
         ▼
┌─────────────────┐
│   PostgreSQL     │◄──────────┐
│   (Render)       │           │
└────────┬─────────┘           │
         │ reads/writes        │ reads/writes
         ▼                     │
┌─────────────────┐            │
│  Spring Boot API │────────────┘
│  - JobController │
│  - ResumeController
│  - ResumeService  │───► Gemini API (scoring)
└────────┬─────────┘
         │ JSON
         ▼
┌─────────────────┐
│  HTML/JS Frontend│
└─────────────────┘
```

## Features

- **Job aggregation** — automatically scrapes and deduplicates remote job listings
- **Resume upload** — extracts text from PDF resumes
- **AI-powered matching** — uses Gemini to semantically compare resumes against job descriptions (not just keyword matching)
- **Match scoring** — returns a 0–100 score per job, with matched skills and identified gaps
- **Ranked results** — jobs sorted by best fit first

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/jobs` | List all jobs |
| GET | `/api/jobs/{id}` | Get a single job |
| GET | `/api/jobs/search?keyword=` | Search jobs by title/description |
| DELETE | `/api/jobs/{id}` | Delete a job |
| POST | `/api/resumes/upload` | Upload a resume PDF, returns resume ID |
| GET | `/api/resumes` | List all resumes |
| GET | `/api/resumes/{id}` | Get a single resume |
| GET | `/api/resumes/{id}/match` | Get ranked job matches for a resume |

## Local Setup

### Prerequisites

- Java 21
- Python 3.9+
- A PostgreSQL database (local or hosted, e.g. [Render](https://render.com))
- A [Gemini API key](https://aistudio.google.com)

### 1. Clone the repo

```bash
git clone https://github.com/anu-raj-16
cd NexusFeed
```

### 2. Set up the database

Create a PostgreSQL database and note its connection URL.

### 3. Configure the backend

In `nexus-backend/src/main/resources/application.properties`, set:

```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
GEMINI_API_KEY=${GEMINI_API_KEY}
```

Set the following environment variables before running:

```bash
export DATABASE_URL=jdbc:postgresql://user:password@host:5432/dbname
export GEMINI_API_KEY=your-gemini-api-key
```

### 4. Run the backend

```bash
cd nexus-backend
mvn spring-boot:run
```

The API will start on `http://localhost:8080`.

### 5. Set up the Python scraper

```bash
python -m venv venv
source venv/bin/activate
pip install feedparser beautifulsoup4 psycopg2-binary
```

Set the database connection variable:

```bash
export PG_URL=postgresql://user:password@host:5432/dbname
```

Run the scraper:

```bash
python main.py
```

### 6. Open the frontend

Open `index.html` in your browser, or serve it with any static file server. Update the API URLs inside the file to point to `http://localhost:8080` if running locally.

## Automated Job Sync

Job scraping runs automatically once a day via a GitHub Actions workflow (`.github/workflows/daily-sync.yml`). It can also be triggered manually from the Actions tab.

## Testing

The backend includes unit tests covering controllers and service logic, using JUnit 5, Mockito, and MockMvc.

```bash
mvn test
```

## Future Improvements

- User authentication so multiple users can manage their own resumes
- Caching match scores to avoid re-scoring on every request
- Additional job sources beyond We Work Remotely
- Parallelized scoring to reduce match response time

## License

This project is for personal/educational use.