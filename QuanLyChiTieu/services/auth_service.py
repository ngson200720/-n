import os
import pandas as pd


BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DATA_DIR = os.path.join(BASE_DIR, "data")
FILE = os.path.join(DATA_DIR, "users.csv")


class AuthService:
    def __init__(self):
        os.makedirs(DATA_DIR, exist_ok=True)

        if not os.path.exists(FILE):
            df = pd.DataFrame(columns=["username", "password"])
            df.to_csv(FILE, index=False)

    def _read_users(self):
        return pd.read_csv(FILE, dtype=str).fillna("")

    def register(self, username, password):
        username = str(username).strip()
        password = str(password).strip()

        if username == "" or password == "":
            return "EMPTY"

        df = self._read_users()

        if username in df["username"].values:
            return "EXISTS"

        new_user = pd.DataFrame(
            [{
                "username": username,
                "password": password
            }]
        )

        df = pd.concat([df, new_user], ignore_index=True)
        df.to_csv(FILE, index=False)

        return "OK"

    def login(self, username, password):
        username = str(username).strip()
        password = str(password).strip()

        if username == "" or password == "":
            return False

        df = self._read_users()

        result = df[
            (df["username"] == username) &
            (df["password"] == password)
        ]

        return not result.empty