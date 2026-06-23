import tkinter as tk
from tkinter import messagebox

from services.auth_service import AuthService


class LoginWindow:
    def __init__(self, root):
        self.root = root
        self.auth = AuthService()

        self.root.title("Đăng nhập")
        self.root.geometry("380x300")
        self.root.resizable(False, False)

        self.show_login_form()

    def clear_window(self):
        for widget in self.root.winfo_children():
            widget.destroy()

    def show_login_form(self):
        self.clear_window()

        frame = tk.Frame(self.root, padx=30, pady=25)
        frame.pack(fill="both", expand=True)

        tk.Label(
            frame,
            text="QUẢN LÝ CHI TIÊU",
            font=("Arial", 16, "bold")
        ).pack(pady=(0, 20))

        tk.Label(
            frame,
            text="Username",
            font=("Arial", 10)
        ).pack(anchor="w")

        self.username_entry = tk.Entry(frame, width=35)
        self.username_entry.pack(pady=(3, 12))

        tk.Label(
            frame,
            text="Password",
            font=("Arial", 10)
        ).pack(anchor="w")

        self.password_entry = tk.Entry(frame, width=35, show="*")
        self.password_entry.pack(pady=(3, 18))

        tk.Button(
            frame,
            text="Đăng nhập",
            width=25,
            command=self.login
        ).pack(pady=4)

        tk.Button(
            frame,
            text="Đăng ký tài khoản",
            width=25,
            command=self.open_register_window
        ).pack(pady=4)

        self.username_entry.focus()

        self.root.bind("<Return>", lambda event: self.login())

    def login(self):
        username = self.username_entry.get().strip()
        password = self.password_entry.get().strip()

        if username == "" or password == "":
            messagebox.showwarning(
                "Thiếu thông tin",
                "Vui lòng nhập username và password"
            )
            return

        if self.auth.login(username, password):
            messagebox.showinfo(
                "Thành công",
                "Đăng nhập thành công"
            )

            self.clear_window()

            from ui.dashboard import Dashboard
            Dashboard(self.root)

        else:
            messagebox.showerror(
                "Lỗi",
                "Sai username hoặc password"
            )

    def open_register_window(self):
        register_window = tk.Toplevel(self.root)
        register_window.title("Đăng ký tài khoản")
        register_window.geometry("360x300")
        register_window.resizable(False, False)

        frame = tk.Frame(register_window, padx=30, pady=25)
        frame.pack(fill="both", expand=True)

        tk.Label(
            frame,
            text="ĐĂNG KÝ TÀI KHOẢN",
            font=("Arial", 14, "bold")
        ).pack(pady=(0, 18))

        tk.Label(frame, text="Username").pack(anchor="w")
        username_entry = tk.Entry(frame, width=35)
        username_entry.pack(pady=(3, 10))

        tk.Label(frame, text="Password").pack(anchor="w")
        password_entry = tk.Entry(frame, width=35, show="*")
        password_entry.pack(pady=(3, 10))

        tk.Label(frame, text="Nhập lại password").pack(anchor="w")
        confirm_entry = tk.Entry(frame, width=35, show="*")
        confirm_entry.pack(pady=(3, 18))

        def register_account():
            username = username_entry.get().strip()
            password = password_entry.get().strip()
            confirm = confirm_entry.get().strip()

            if username == "" or password == "":
                messagebox.showwarning(
                    "Thiếu thông tin",
                    "Vui lòng nhập username và password"
                )
                return

            if password != confirm:
                messagebox.showerror(
                    "Lỗi",
                    "Mật khẩu nhập lại không khớp"
                )
                return

            result = self.auth.register(username, password)

            if result == "OK":
                messagebox.showinfo(
                    "Thành công",
                    "Đăng ký tài khoản thành công"
                )

                self.username_entry.delete(0, tk.END)
                self.username_entry.insert(0, username)

                self.password_entry.delete(0, tk.END)
                self.password_entry.focus()

                register_window.destroy()

            elif result == "EXISTS":
                messagebox.showerror(
                    "Lỗi",
                    "Username đã tồn tại"
                )

            elif result == "EMPTY":
                messagebox.showwarning(
                    "Thiếu thông tin",
                    "Vui lòng nhập đầy đủ username và password"
                )

        tk.Button(
            frame,
            text="Đăng ký",
            width=25,
            command=register_account
        ).pack()

        username_entry.focus()