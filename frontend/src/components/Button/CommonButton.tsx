export function CommonButton({ active, icon, onClick, children, className }: BtnProps) {
  const bgColor = active ? 'bg-[#2C365B]' : 'bg-[#2C365B]';
  const textColor = active ? 'text-white' : 'text-white';

  return (
    <div
      className={`${className} font-pretendard flex flex-row items-center rounded-3xl ${bgColor} ${textColor}  hover:cursor-pointer`}
      onClick={onClick}
    >
      {icon && <div className="pr-2">{icon}</div>}
      <div className="flex-1 text-center">{children}</div>
    </div>
  );
}
