import { useAtom } from 'jotai';
import { currentPageAtom, totalPagesAtom, updatePagination } from '@store/pagination';

const Pagination = () => {
  const [currentPage, setCurrentPage] = useAtom(currentPageAtom);
  const [totalPages, setTotalPages] = useAtom(totalPagesAtom);
  const totalDisplayedPages = 10;

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
    updatePagination(setTotalPages, totalPages, page);
  };

  const getPaginationItems = () => {
    const items = [];

    if (totalPages <= totalDisplayedPages) {
      for (let i = 1; i <= totalPages; i++) {
        items.push(i);
      }
    } else {
      const leftPages = 3;
      const rightPages = 3;

      items.push(1);

      if (currentPage > leftPages + 2) {
        for (let i = Math.max(2, currentPage - leftPages); i < currentPage; i++) {
          items.push(i);
        }
        items.push('...');
      }

      items.push(currentPage);

      if (currentPage < totalPages - rightPages - 1) {
        for (
          let i = currentPage + 1;
          i <= Math.min(currentPage + rightPages, totalPages - 1);
          i++
        ) {
          items.push(i);
        }
        items.push(totalPages);
      } else {
        for (let i = currentPage + 1; i <= totalPages; i++) {
          items.push(i);
        }
      }
    }

    return items;
  };

  const paginationItems = getPaginationItems();

  return (
    <div className="flex justify-center mt-4">
      <button
        className="px-4 py-2 bg-gray-200 rounded mx-1"
        onClick={() => handlePageChange(Math.max(currentPage - 1, 1))}
        disabled={currentPage === 1}
      >
        이전
      </button>

      {paginationItems.map((item, index) => (
        <button
          key={index}
          className={`px-4 py-2 ${item === currentPage ? 'bg-blue-500 text-white' : 'bg-gray-200'} rounded mx-1`}
          onClick={() => typeof item === 'number' && handlePageChange(item)}
          disabled={item === '...'}
        >
          {item}
        </button>
      ))}

      <button
        className="px-4 py-2 bg-gray-200 rounded mx-1"
        onClick={() => handlePageChange(Math.min(currentPage + 1, totalPages))}
        disabled={currentPage === totalPages}
      >
        다음
      </button>
    </div>
  );
};

export default Pagination;
