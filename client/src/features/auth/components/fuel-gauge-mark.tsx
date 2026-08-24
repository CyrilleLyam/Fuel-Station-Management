const CENTER = { x: 100, y: 120 };
const ARC_RADIUS = 82;
const TICK_INNER_RADIUS = 68;
const LABEL_RADIUS = 98;
const NEEDLE_LENGTH = 60;
const NEEDLE_HALF_WIDTH = 4;
const TICKS = [0, 0.25, 0.5, 0.75, 1];
const NEEDLE_T = 0.82;

function polar(t: number, radius: number) {
  const angle = ((180 + 180 * t) * Math.PI) / 180;
  return {
    x: CENTER.x + radius * Math.cos(angle),
    y: CENTER.y + radius * Math.sin(angle),
  };
}

function arcPath(radius: number, steps = 48) {
  return Array.from({ length: steps + 1 }, (_, i) => polar(i / steps, radius))
    .map((p, i) => `${i === 0 ? "M" : "L"}${p.x.toFixed(2)},${p.y.toFixed(2)}`)
    .join(" ");
}

export function FuelGaugeMark({ className }: { className?: string }) {
  const needleAngle = ((180 + 180 * NEEDLE_T) * Math.PI) / 180;
  const tip = polar(NEEDLE_T, NEEDLE_LENGTH);
  const perp = {
    x: -Math.sin(needleAngle) * NEEDLE_HALF_WIDTH,
    y: Math.cos(needleAngle) * NEEDLE_HALF_WIDTH,
  };
  const eLabel = polar(0, LABEL_RADIUS);
  const fLabel = polar(1, LABEL_RADIUS);

  return (
    <svg viewBox="0 0 200 140" className={className} aria-hidden="true">
      <path
        d={arcPath(ARC_RADIUS)}
        fill="none"
        stroke="#C89B3C"
        strokeWidth={2.5}
        strokeLinecap="round"
      />
      {TICKS.map((t) => {
        const outer = polar(t, ARC_RADIUS);
        const inner = polar(t, TICK_INNER_RADIUS);
        return (
          <line
            key={t}
            x1={outer.x}
            y1={outer.y}
            x2={inner.x}
            y2={inner.y}
            stroke="#C89B3C"
            strokeWidth={t === 0 || t === 1 ? 2.5 : 1.5}
            strokeLinecap="round"
            opacity={t === 0 || t === 1 ? 1 : 0.5}
          />
        );
      })}
      <text
        x={eLabel.x}
        y={eLabel.y}
        fill="#E8E3D8"
        fontSize="13"
        fontFamily="'JetBrains Mono Variable', monospace"
        textAnchor="middle"
        dominantBaseline="middle"
      >
        E
      </text>
      <text
        x={fLabel.x}
        y={fLabel.y}
        fill="#E8E3D8"
        fontSize="13"
        fontFamily="'JetBrains Mono Variable', monospace"
        textAnchor="middle"
        dominantBaseline="middle"
      >
        F
      </text>
      <polygon
        points={`${CENTER.x + perp.x},${CENTER.y + perp.y} ${CENTER.x - perp.x},${CENTER.y - perp.y} ${tip.x},${tip.y}`}
        fill="#FF5A1F"
      />
      <circle cx={CENTER.x} cy={CENTER.y} r={8} fill="#C89B3C" />
      <circle cx={CENTER.x} cy={CENTER.y} r={3} fill="#14110F" />
    </svg>
  );
}
