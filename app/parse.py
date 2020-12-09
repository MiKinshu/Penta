import requests
from cleantext import clean
from bs4 import BeautifulSoup

def get_text(URL):
    r = requests.get(URL)

    soup = BeautifulSoup(r.content, 'html5lib')

    cont = soup.findAll('p')
    txt = [c.getText() for c in cont]
    full = ''
    for c in txt:
        full = full + c
    full = full.replace('\n', ' ')
    clean(full, no_line_breaks=True)
    return full
