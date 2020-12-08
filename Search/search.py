import spacy
import PyPDF2
from spacy.matcher import PhraseMatcher
from flask import Flask

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

# spacy english model medium
nlp = spacy.load('en_core_web_md')

txt = ''
with open('cricket.txt', 'r') as f:
    txt = f.read()

doc = nlp(txt)
# keyword = 'Which is the most popular format of cricket?'
# keyword = 'How many players are there in baseball team?'
# keyword = 'What countries participate in baseball?'
# keyword = 'How is baseball played?'
print(get_simil(keyword, doc))