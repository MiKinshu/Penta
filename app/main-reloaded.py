import nltk
from parse import get_text
from sentence_transformers import SentenceTransformer, util
import torch

tokenizer = nltk.data.load('tokenizers/punkt/english.pickle')
txt = get_text('https://www.cs.purdue.edu/homes/hosking/cricket/explanation.htm')
corpus = tokenizer.tokenize(txt)
embedder = SentenceTransformer('distilbert-base-nli-stsb-mean-tokens')
corpus_embeddings = embedder.encode(corpus, convert_to_tensor=True)

queries = ["How to play cricket?"]
top_k = 5
for query in queries:
    query_embedding = embedder.encode(query, convert_to_tensor=True)
    cos_scores = util.pytorch_cos_sim(query_embedding, corpus_embeddings)[0]
    cos_scores = cos_scores.cpu()

    #We use torch.topk to find the highest 5 scores
    top_results = torch.topk(cos_scores, k=top_k)

    print("\n\n======================\n\n")
    print("Query:", query)
    print("\nTop 5 most similar sentences in corpus:")

    for score, idx in zip(top_results[0], top_results[1]):
        print(corpus[idx], "(Score: %.4f)" % (score))