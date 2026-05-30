import pandas as pd

FILE = "data/transactions.csv"


class ReportService:

    def summary(self):

        df = pd.read_csv(FILE)

        income = df[
            df["Type"] == "Thu"
        ]["Amount"].sum()

        expense = df[
            df["Type"] == "Chi"
        ]["Amount"].sum()

        balance = income - expense

        return (
            income,
            expense,
            balance
        )

    def export_excel(self):

        df = pd.read_csv(FILE)

        df.to_excel(
            "exports/BaoCao.xlsx",
            index=False
        )