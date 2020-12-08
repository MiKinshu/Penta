bundle: cd app; python -m spacy download en_core_web_sm; cd ..
web: gunicorn --chdir /app/app --bind 0.0.0.0:$PORT main:app