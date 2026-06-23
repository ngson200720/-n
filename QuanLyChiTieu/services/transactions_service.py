import os
import pandas as pd


BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DATA_DIR = os.path.join(BASE_DIR, "data")
FILE = os.path.join(DATA_DIR, "transactions.csv")


class TransactionService:
    def __init__(self):
        os.makedirs(DATA_DIR, exist_ok=True)

        if not os.path.exists(FILE):
            df = pd.DataFrame(
                columns=[
                    "ID",
                    "Date",
                    "Type",
                    "Category",
                    "Amount",
                    "Note"
                ]
            )
            df.to_csv(FILE, index=False)

    def get_all(self):
        return pd.read_csv(FILE).fillna("")

    def add(self, transaction):
        df = pd.read_csv(FILE)

        new_row = pd.DataFrame(
            [transaction.to_dict()]
        )

        df = pd.concat(
            [df, new_row],
            ignore_index=True
        )

        df.to_csv(FILE, index=False)

    def delete(self, trans_id):
        df = pd.read_csv(FILE)

        df = df[
            df["ID"].astype(str) != str(trans_id)
        ]

        df.to_csv(FILE, index=False)