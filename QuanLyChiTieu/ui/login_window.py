import tkinter as tk
from tkinter import messagebox

from services.auth_service import AuthService


class LoginWindow:

    def __init__(self, root):

        self.root = root

        self.auth = AuthService()

        self.root.title("Đăng nhập")

        tk.Label(
            root,
            text="Username"
        ).pack()

        self.username = tk.Entry(root)
        self.username.pack()

        tk.Label(
            root,
            text="Password"
        ).pack()

        self.password = tk.Entry(
            root,
            show="*"
        )

        self.password.pack()

        tk.Button(
            root,
            text="Đăng nhập",
            command=self.login
        ).pack()

    def login(self):

        user = self.username.get()
        pwd = self.password.get()

        if self.auth.login(
            user,
            pwd
        ):
            messagebox.showinfo(
                "OK",
                "Đăng nhập thành công"
            )
        else:
            messagebox.showerror(
                "Lỗi",
                "Sai tài khoản"
            )