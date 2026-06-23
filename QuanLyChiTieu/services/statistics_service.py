import os
import pandas as pd


BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DATA_DIR = os.path.join(BASE_DIR, "data")
FILE = os.path.join(DATA_DIR, "transactions.csv")


class StatisticsService:
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

    def read_data(self):
        df = pd.read_csv(FILE).fillna("")

        if df.empty:
            return df

        df["Amount"] = pd.to_numeric(
            df["Amount"],
            errors="coerce"
        ).fillna(0)

        df["Date"] = pd.to_datetime(
            df["Date"],
            errors="coerce"
        )

        df = df.dropna(subset=["Date"])

        return df

    def overview(self):
        df = self.read_data()

        if df.empty:
            return {
                "income": 0,
                "expense": 0,
                "balance": 0,
                "transaction_count": 0,
                "avg_expense": 0,
                "max_expense": 0,
                "min_expense": 0
            }

        income = df[df["Type"] == "Thu"]["Amount"].sum()
        expense_df = df[df["Type"] == "Chi"]
        expense = expense_df["Amount"].sum()
        balance = income - expense

        avg_expense = expense_df["Amount"].mean() if not expense_df.empty else 0
        max_expense = expense_df["Amount"].max() if not expense_df.empty else 0
        min_expense = expense_df["Amount"].min() if not expense_df.empty else 0

        return {
            "income": income,
            "expense": expense,
            "balance": balance,
            "transaction_count": len(df),
            "avg_expense": avg_expense,
            "max_expense": max_expense,
            "min_expense": min_expense
        }

    def expense_by_category(self):
        df = self.read_data()

        if df.empty:
            return pd.DataFrame(columns=["Category", "Amount"])

        expense_df = df[df["Type"] == "Chi"]

        if expense_df.empty:
            return pd.DataFrame(columns=["Category", "Amount"])

        result = (
            expense_df
            .groupby("Category")["Amount"]
            .sum()
            .reset_index()
            .sort_values(by="Amount", ascending=False)
        )

        return result

    def income_expense_by_type(self):
        df = self.read_data()

        if df.empty:
            return pd.DataFrame(columns=["Type", "Amount"])

        result = (
            df
            .groupby("Type")["Amount"]
            .sum()
            .reset_index()
            .sort_values(by="Amount", ascending=False)
        )

        return result

    def income_expense_by_date(self):
        df = self.read_data()

        if df.empty:
            return pd.DataFrame(columns=["Date", "Thu", "Chi"])

        pivot = (
            df
            .pivot_table(
                index="Date",
                columns="Type",
                values="Amount",
                aggfunc="sum",
                fill_value=0
            )
            .reset_index()
            .sort_values(by="Date")
        )

        if "Thu" not in pivot.columns:
            pivot["Thu"] = 0

        if "Chi" not in pivot.columns:
            pivot["Chi"] = 0

        return pivot[["Date", "Thu", "Chi"]]

    def monthly_summary(self):
        df = self.read_data()

        if df.empty:
            return pd.DataFrame(columns=["Month", "Thu", "Chi", "Balance"])

        df["Month"] = df["Date"].dt.strftime("%Y-%m")

        pivot = (
            df
            .pivot_table(
                index="Month",
                columns="Type",
                values="Amount",
                aggfunc="sum",
                fill_value=0
            )
            .reset_index()
            .sort_values(by="Month")
        )

        if "Thu" not in pivot.columns:
            pivot["Thu"] = 0

        if "Chi" not in pivot.columns:
            pivot["Chi"] = 0

        pivot["Balance"] = pivot["Thu"] - pivot["Chi"]

        return pivot[["Month", "Thu", "Chi", "Balance"]]