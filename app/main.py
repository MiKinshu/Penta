import spacy
import PyPDF2
from spacy.matcher import PhraseMatcher
from parse import readPdfFile
from flask import Flask, request
app = Flask(__name__)

try:
    nlp = spacy.load('en_core_web_md')
except:
    pass

def get_simil(keyword, doc):
    keys = nlp(keyword)
    pq = []
    for sent in doc.sents:
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
    if request.content_type.startswith('multipart/form-data'):
        data = request.form['data']
        query = request.form['query']
        doc = nlp(data)
        ans = get_simil(query, doc)
        return ans
    elif request.content_type == 'application/json':
        json = request.get_json()
        data = json['data']
        query = json['query']
        doc = nlp(data)
        ans = get_simil(query, doc)
        return ans
    else:
        print(request.content_type)
        return "Content type not supported"

if __name__ == '__main__':
    app.run()