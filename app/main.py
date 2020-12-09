import spacy
from sentence_transformers import SentenceTransformer, util
import torch
from parse import get_text
import nltk
from flask import Flask, request

app = Flask(__name__)
tokenizer = nltk.data.load('tokenizers/punkt/english.pickle')
nlp = spacy.load('en_core_web_md')
embedder = SentenceTransformer('distilbert-base-nli-stsb-mean-tokens')

def get_simil_2(query, corpus, top_k = 5):
    corpus_embeddings = embedder.encode(corpus, convert_to_tensor=True)
    query_embedding = embedder.encode(query, convert_to_tensor=True)
    cos_scores = util.pytorch_cos_sim(query_embedding, corpus_embeddings)[0]
    cos_scores = cos_scores.cpu()
    top_results = torch.topk(cos_scores, k=top_k)
    ans = ''
    for score, idx in zip(top_results[0], top_results[1]):
        ans += corpus[idx]
    return ans

def get_simil(keyword, data):
    keys = nlp(keyword)
    pq = []
    for sent in data:
        sent = nlp(sent)
        tot = sent.similarity(keys)
        pq.append((tot, sent))
    pq.sort(reverse=True)
    ans = ''
    cut = max(5, int(len(pq)*0.1))
    for i in range(cut):
        ans += pq[i][1].text
    return ans

@app.route('/', methods=['POST'])
def index():
    data, query = None, None
    if request.content_type.startswith('multipart/form-data') or request.content_type=='application/x-www-form-urlencoded':
        data = request.form['data']
        query = request.form['query']
    elif request.content_type == 'application/json':
        json = request.get_json()
        data = json['data']
        query = json['query']
    else:
        print(request.content_type)
        return "Content type not supported"
    if len(data)> 5 and data[0:4] == 'http':
        data = get_text(data)
    data = tokenizer.tokenize(data)
    ans = get_simil_2(query, data)
    return ans

if __name__ == '__main__':
    app.run()