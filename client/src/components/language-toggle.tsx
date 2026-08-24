import { useTranslation } from "react-i18next";
import { Button } from "@/components/ui/button";

export function LanguageToggle({ className }: { className?: string }) {
  const { i18n } = useTranslation();
  const isKhmer = i18n.resolvedLanguage === "km";

  function toggle() {
    i18n.changeLanguage(isKhmer ? "en" : "km");
  }

  return (
    <Button
      type="button"
      variant="outline"
      size="sm"
      onClick={toggle}
      className={className}
      aria-label={isKhmer ? "Switch to English" : "ប្តូរទៅភាសាខ្មែរ"}
    >
      {isKhmer ? "EN" : "ខ្មែរ"}
    </Button>
  );
}
