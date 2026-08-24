import { FuelGaugeMark } from "./fuel-gauge-mark";

export function AuthBrandPanel() {
  return (
    <div className="relative hidden overflow-hidden bg-[#14110F] lg:flex lg:flex-col lg:justify-between lg:p-12 xl:p-16">
      <div className="flex flex-col gap-10">
        <FuelGaugeMark className="h-28 w-36" />
        <div className="flex flex-col gap-3">
          <span className="font-jetbrains text-xs tracking-[0.3em] text-[#C89B3C] uppercase">
            Operations console
          </span>
          <h1 className="font-display text-4xl leading-[1.05] font-medium tracking-tight text-[#F6F3EC] uppercase xl:text-5xl">
            Fuel Station
            <br />
            Management
          </h1>
          <p className="max-w-sm text-sm leading-relaxed text-[#8B8578]">
            Real-time visibility across every pump, tank and terminal — from
            the first shift to the last.
          </p>
        </div>
      </div>
      <div className="flex items-center gap-3 font-jetbrains text-xs tracking-[0.2em] text-[#6B6459] uppercase">
        <span>Status: online</span>
        <span className="h-1 w-1 rounded-full bg-[#C89B3C]" />
        <span>v1.0</span>
      </div>
      <div
        className="absolute inset-x-0 bottom-0 h-2"
        style={{
          background:
            "repeating-linear-gradient(135deg, #FF5A1F 0 14px, #14110F 14px 28px)",
        }}
      />
    </div>
  );
}

export function MobileAuthBrandBar() {
  return (
    <div className="flex items-center gap-3 border-b border-border px-6 py-4 lg:hidden">
      <FuelGaugeMark className="h-9 w-11 shrink-0 [&_text]:fill-muted-foreground" />
      <span className="font-display text-sm font-medium tracking-tight uppercase">
        Fuel Station Management
      </span>
    </div>
  );
}
