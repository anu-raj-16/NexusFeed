import feedparser
import ssl
import gc
from bs4 import BeautifulSoup

# Fix for some Mac/Windows SSL certificate issues
if hasattr(ssl, '_create_unverified_context'):
    ssl._create_default_https_context = ssl._create_unverified_context

def clean_html(raw_html):
    if not raw_html:
        return ""
    soup = BeautifulSoup(raw_html, 'html.parser')
    
    # The 'separator' ensures words don't stick together when tags are removed
    # The 'strip=True' removes extra whitespace/newlines
    return soup.get_text(separator=" ", strip=True)

def agg_jobs():
# 3. Specific Error Checking
    url = "https://weworkremotely.com/remote-jobs.rss"
    feed = feedparser.parse(url)
    # 1. Identify yourself (Recruiters love this professionalism)
    user_agent = "NexusFeed-Bot/1.0 (Contact: your-email@example.com)"

    # 2. Try the connection with the header
    print(f"Connecting to {url}...")
    feed = feedparser.parse(url, agent=user_agent)

    try:
        if feed.bozo:
            raise feed.bozo_exception
        
        print(f"Success! Feed Title: {feed.feed.get('title', 'N/A')}")

        matches = []

        for entry in feed.entries:
            # print(f"- {entry.title}")
            # target_skills = ["python", "java", "intern", "backend", "software", "developer"]
            entry_info = entry.get('title', 'N/A') + " " + entry.get('description', 'N/A')
            target_skills = ["python", "java"]
            for skill in target_skills:
                if (skill in entry_info.lower()) and ("intern" in entry_info.lower()):
                    entry_title = clean_html(entry.get('title', 'N/A'))
                    entry_desc = clean_html(entry.get('description', 'N/A'))
                    entry_pub = clean_html(entry.get('published', 'N/A'))
                    # job_data = {
                    #     "title": str(entry.get('title', 'N/A')),
                    #     "description": str(entry.get('description', 'N/A')),
                    #     "source": "We Work Remotely",
                    #     "published": str(entry.get('published', 'N/A'))
                    # }
                    job_data = {
                        "title": entry_title,
                        "description": entry_desc,
                        "source": "We Work Remotely",
                        "published": entry_pub
                    }
                    matches.append(job_data)
                
        print(f"Stored {len(matches)} relevant jobs.")

        return matches

    except Exception as e:
        # 3. Handle the parsing error here
        print(f"Parsing failed or feed is malformed: {e}")
    
    finally:
        del feed
        gc.collect()

    
    




