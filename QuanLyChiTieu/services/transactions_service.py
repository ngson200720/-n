import pandas as pd
import os

FILE = "data/transactions.csv"


class TransactionService:

    def __init__(self):

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

            df.to_csv(
                FILE,
                index=False
            )

    def get_all(self):
        return pd.read_csv(FILE)

    def add(self, transaction):

        df = pd.read_csv(FILE)

        new_row = pd.DataFrame(
            [transaction.to_dict()]
        )

        df = pd.concat(
            [df, new_row],
            ignore_index=True
        )

        df.to_csv(
            FILE,
            index=False
        )

    def delete(self, trans_id):

        df = pd.read_csv(FILE)

        df = df[
            df["ID"] != trans_id
        ]

        df.to_csv(
            FILE,
            index=False
        )