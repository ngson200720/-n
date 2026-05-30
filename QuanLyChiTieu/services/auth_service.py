import pandas as pd
import os

FILE = "data/users.csv"


class AuthService:

    def __init__(self):
        if not os.path.exists(FILE):
            df = pd.DataFrame(
                columns=[
                    "username",
                    "password"
                ]
            )
            df.to_csv(FILE, index=False)

    def register(
        self,
        username,
        password
    ):

        df = pd.read_csv(FILE)

        if username in df["username"].values:
            return False

        new_user = pd.DataFrame(
            [{
                "username": username,
                "password": password
            }]
        )

        df = pd.concat(
            [df, new_user],
            ignore_index=True
        )

        df.to_csv(FILE, index=False)

        return True

    def login(
        self,
        username,
        password
    ):

        df = pd.read_csv(FILE)

        result = df[
            (df["username"] == username)
            &
            (df["password"] == password)
        ]

        return not result.empty