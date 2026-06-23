class Transaction:
    def __init__(
        self,
        trans_id,
        date,
        trans_type,
        category,
        amount,
        note
    ):
        self.trans_id = trans_id
        self.date = date
        self.trans_type = trans_type
        self.category = category
        self.amount = float(amount)
        self.note = note

    def to_dict(self):
        return {
            "ID": self.trans_id,
            "Date": self.date,
            "Type": self.trans_type,
            "Category": self.category,
            "Amount": self.amount,
            "Note": self.note
        }