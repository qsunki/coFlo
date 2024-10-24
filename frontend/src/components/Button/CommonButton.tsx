export function CommonButton({ active, icon, children, className }: BtnProps) {
  const bgColor = active ? 'bg-[#2C365B]' : 'bg-[#2C365B]';
  const textColor = active ? 'text-[#707A9F]' : 'text-[FFFFFF]';

  return (
    <div
      className={`${className} font-pretentard flex flex-row items-center rounded-[3px] w-full h-10 ${bgColor} ${textColor} px-3 py-2 select-none hover:cursor-pointer`}
    >
      <div>{icon}</div>
      <div className="pl-3">{children}</div>
    </div>
  );
}
