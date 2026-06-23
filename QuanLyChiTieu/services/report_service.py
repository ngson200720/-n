import os
import pandas as pd


BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DATA_DIR = os.path.join(BASE_DIR, "data")
EXPORT_DIR = os.path.join(BASE_DIR, "exports")
FILE = os.path.join(DATA_DIR, "transactions.csv")


class ReportService:
    def __init__(self):
        os.makedirs(DATA_DIR, exist_ok=True)
        os.makedirs(EXPORT_DIR, exist_ok=True)

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

        df["Amount"] = pd.to_numeric(df["Amount"], errors="coerce").fillna(0)
        return df

    def summary(self):
        df = self.read_data()

        if df.empty:
            return {
                "income": 0,
                "expense": 0,
                "balance": 0,
                "transaction_count": 0
            }

        income = df[df["Type"] == "Thu"]["Amount"].sum()
        expense = df[df["Type"] == "Chi"]["Amount"].sum()
        balance = income - expense

        return {
            "income": income,
            "expense": expense,
            "balance": balance,
            "transaction_count": len(df)
        }

    def category_report(self):
        df = self.read_data()

        if df.empty:
            return pd.DataFrame(columns=["Category", "Total"])

        expense = df[df["Type"] == "Chi"]

        if expense.empty:
            return pd.DataFrame(columns=["Category", "Total"])

        result = (
            expense
            .groupby("Category")["Amount"]
            .sum()
            .reset_index()
            .sort_values(by="Amount", ascending=False)
        )

        result.columns = ["Category", "Total"]

        return result

    def type_report(self):
        df = self.read_data()

        if df.empty:
            return pd.DataFrame(columns=["Type", "Total"])

        result = (
            df
            .groupby("Type")["Amount"]
            .sum()
            .reset_index()
            .sort_values(by="Amount", ascending=False)
        )

        result.columns = ["Type", "Total"]

        return result

    def export_excel(self):
        df = self.read_data()
        summary = self.summary()
        category_df = self.category_report()
        type_df = self.type_report()

        export_file = os.path.join(EXPORT_DIR, "BaoCaoChiTieu.xlsx")

        summary_df = pd.DataFrame(
            [
                ["Tổng thu", summary["income"]],
                ["Tổng chi", summary["expense"]],
                ["Số dư", summary["balance"]],
                ["Số giao dịch", summary["transaction_count"]]
            ],
            columns=["Chỉ tiêu", "Giá trị"]
        )

        with pd.ExcelWriter(export_file, engine="openpyxl") as writer:
            df.to_excel(writer, sheet_name="Du_lieu_goc", index=False)
            summary_df.to_excel(writer, sheet_name="Tong_quan", index=False)
            category_df.to_excel(writer, sheet_name="Chi_theo_danh_muc", index=False)
            type_df.to_excel(writer, sheet_name="Thu_chi", index=False)

        return export_file