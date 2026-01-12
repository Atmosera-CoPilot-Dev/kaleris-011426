from abc import ABC, abstractmethod
from typing import List


class IReportTemplate(ABC):
    """
    IReportTemplate defines the skeleton of an algorithm using the Template Method Pattern.
    """

    def render_title(self, title: str) -> None:
        print(title)

    def render_headings(self, headings: List[str]) -> None:
        print("\t".join(headings))

    def render_rows(self, rows: List[List[str]]) -> None:
        for row in rows:
            print("\t".join(row))
        print()

    def render_footer(self, footer: str) -> None:
        print(footer)

    @abstractmethod
    def get_title(self) -> str:
        pass

    @abstractmethod
    def get_headings(self) -> List[str]:
        pass

    @abstractmethod
    def get_rows(self) -> List[List[str]]:
        pass

    @abstractmethod
    def get_footer(self) -> str:
        pass

    def template_method(self) -> None:
        """
        Template method defining the skeleton of the algorithm.
        """
        self.render_title(self.get_title())
        self.render_headings(self.get_headings())
        self.render_rows(self.get_rows())
        self.render_footer(self.get_footer())


class CompanyReport(IReportTemplate):
    """
    CompanyReport implements the primitive operations to carry out specific steps
    of the algorithm.
    """

    def get_title(self) -> str:
        return "Scoppa Software Solutions, LLC"

    def get_headings(self) -> List[str]:
        return ["SKU", "PRICE"]

    def get_rows(self) -> List[List[str]]:
        return [
            ["a01d", "$12.99"],
            ["a7cd", "$15.99"],
            ["a9cb", "$18.99"]
        ]

    def get_footer(self) -> str:
        return "All rights reserved"


def main() -> None:
    report = CompanyReport()
    report.template_method()


if __name__ == "__main__":
    main()