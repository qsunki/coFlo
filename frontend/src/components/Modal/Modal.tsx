export const Modal = ({ repo, inputValue, setInputValue, onConfirm, onClose }: ModalProps) => {
  return (
    <div className="fixed inset-0 border bg-black bg-opacity-50 flex items-center justify-center">
      <div className="bg-white p-4  border-2 border-[#2C365B] rounded w-[276px] h-[300px]">
        <input
          type="text"
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
          placeholder="토큰을 입력하세요"
          className="border border-gray-300 p-2 rounded w-full"
        />
        <div className="flex justify-end mt-4">
          <button onClick={onClose} className="mr-2">
            닫기
          </button>
          <button onClick={onConfirm} className="bg-blue-500 text-white p-2 rounded">
            확인
          </button>
        </div>
      </div>
    </div>
  );
};
