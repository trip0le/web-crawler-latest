from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import os
import requests
from bs4 import BeautifulSoup
from dotenv import load_dotenv

# Load variables from .env file
load_dotenv()

# Hugging Face API key
HUGGINGFACE_API_KEY = os.getenv("HUGGINGFACE_API_KEY")

app = FastAPI()

class SummarizeRequest(BaseModel):
    url: str

def fetch_and_extract_text(url: str) -> str:
    try:
        response = requests.get(url, timeout=10)
        response.raise_for_status()
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Error fetching URL: {e}")

    soup = BeautifulSoup(response.text, "html.parser")

    # Remove unnecessary tags
    for tag in soup(["script", "style", "nav", "footer", "header", "noscript"]):
        tag.decompose()

    # Extract visible text
    text = soup.get_text(separator="\n")
    lines = [line.strip() for line in text.splitlines() if line.strip()]
    cleaned_text = "\n".join(lines)

    # Limit length for Hugging Face input (e.g., 3000 chars)
    return cleaned_text[:3000]

@app.get("/healthz")
def health_check():
    return {"status": "OK"}

@app.get("/readyz")
def readiness_check():
    # You can add more advanced checks here (e.g., database, cache, etc.)
    return {"status": "Ready"}

@app.post("/summarize")
async def summarize(request: SummarizeRequest):
    try:
        content = fetch_and_extract_text(request.url)

        # If content is too large, truncate it further to fit model limits
        if len(content) > 1000:  # You can adjust the length based on your preference
            content = content[:1000]

        # Hugging Face Summarization API URL
        hf_url = "https://api-inference.huggingface.co/models/facebook/bart-large-cnn"
        headers = {"Authorization": f"Bearer {HUGGINGFACE_API_KEY}"}
        
        # Send the request to Hugging Face's model for summarization
        response = requests.post(hf_url, headers=headers, json={"inputs": content}, verify=False)

        if response.status_code != 200:
            raise HTTPException(status_code=500, detail="Error summarizing text from Hugging Face.")

        summary = response.json()[0]['summary_text']
        return {"summary": summary}

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
