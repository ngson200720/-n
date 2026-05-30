import tkinter as tk
from tkinter import ttk
from tkinter import messagebox

from services.transaction_service import TransactionService
from models.transaction import Transaction


class Dashboard:

    def __init__(self, root):

        self.root = root

        self.service = TransactionService()

        self.root.title(
            "Quản lý chi tiêu"
        )

        self.tree = ttk.Treeview(
            root,
            columns=(
                "ID",
                "Date",
                "Type",
                "Category",
                "Amount"
            ),
            show="headings"
        )

        for c in (
            "ID",
            "Date",
            "Type",
            "Category",
            "Amount"
        ):
            self.tree.heading(
                c,
                text=c
            )

        self.tree.pack(
            fill="both",
            expand=True
        )

        tk.Button(
            root,
            text="Tải dữ liệu",
            command=self.load_data
        ).pack()

    def load_data(self):

        self.tree.delete(
            *self.tree.get_children()
        )

        df = self.service.get_all()

        for _, row in df.iterrows():

            self.tree.insert(
                "",
                "end",
                values=list(row)
            )