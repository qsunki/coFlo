import { useState, useRef, useEffect } from 'react';
import { useAtom } from 'jotai';

import { GitlabIcon } from './Icons/Gitlab';
import { SelectorIcon } from './Icons/Selector';
import ProjectSelector from '@components/Project/ProjectSelector';
import { projectFullPathAtom } from '@store/auth';
import { UserProject } from '@apis/Link';

export function Title() {
  const [isOpen, setIsOpen] = useState(false);
  const titleRef = useRef<HTMLDivElement>(null);
  const [projectFullPath, setProjectFullPath] = useAtom(projectFullPathAtom);

  const handleClick = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setIsOpen(!isOpen);
  };

  const handleClose = () => {
    setIsOpen(false);
  };

  useEffect(() => {
    const fetchProjectInfo = async () => {
      const response = await UserProject.getLinkStatus();
      if (response?.data) {
        const { projectFullPath } = response.data;
        setProjectFullPath(projectFullPath);
      }
    };

    fetchProjectInfo();
  }, []);

  return (
    <div className="relative w-full">
      <div
        className="flex items-center justify-between w-full py-6 cursor-pointer h-12 hover:bg-secondary/10"
        onClick={handleClick}
        ref={titleRef}
      >
        <div className="flex flex-row items-center ">
          <div className="ml-3">
            <GitlabIcon />
          </div>

          <div className="pl-3">
            <div className="text-sm font-bold text-primary-500 truncate max-w-36">
              {projectFullPath}
            </div>
            <div className="text-sm text-gray-600">프로젝트 변경</div>
          </div>
        </div>

        <div className="ml-3">
          <SelectorIcon />
        </div>
      </div>
      {isOpen && (
        <div className="absolute left-64 top-0 z-50 mt-1">
          <ProjectSelector onClose={handleClose} titleRef={titleRef} />
        </div>
      )}
    </div>
  );
}
