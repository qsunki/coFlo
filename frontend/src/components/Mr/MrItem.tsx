import { MrItemProps } from 'types/mergeRequest';
import LabelList from './LabelList';
import useTimeAgo from '@hooks/time';
import { PullRequestIcon } from '@components/TextDiv/Icons/PullRequestIcon';
import { Tooltip } from '@components/ToolTip/ToolTip';

export function MrItem({ mergeRequest, projectLabels }: MrItemProps) {
  const {
    title,
    // state,
    assignee,
    reviewer,
    createdAt,
    updatedAt,
    // sourceBranch,
    targetBranch,
    // description,
    labels,
  } = mergeRequest;

  const createTimeAgo = useTimeAgo(createdAt);
  const updateTimeAgo = useTimeAgo(updatedAt);

  return (
    <div className="pt-5 pb-5 p-2 w-full min-w-[1000px] border-b border-gray-300 font-pretendard">
      <div className="flex flex-row justify-between ">
        <h2 className="text-lg font-semibold">{title}</h2>
      </div>
      <div className="flex flex-row justify-between mt-1">
        <div className="flex items-center">
          <span className="text-xs mr-1">created {createTimeAgo} </span>
          {assignee ? (
            <>
              <span className="text-xs mr-3"> by {assignee.username}</span>
            </>
          ) : (
            <span className="text-xs text-gray-500 mr-3"></span>
          )}
          <PullRequestIcon className="w-3 h-3 flex-shrink-0 mr-1" />
          <span className="text-xs font-bold mr-1">{targetBranch}</span>
        </div>
        <div className="flex items-center">
          {assignee ? (
            <>
              <Tooltip text={`Assigned to ${assignee.username}`}>
                <img
                  src={assignee.avatarUrl}
                  alt={`${assignee.username}의 아바타`}
                  className="w-5 h-5 rounded-full mr-1 cursor-pointer"
                />
              </Tooltip>
            </>
          ) : (
            <span className="text-xs text-gray-500"></span>
          )}
          {reviewer ? (
            <>
              <Tooltip text={`Review requested from ${reviewer.username}`}>
                <img
                  src={reviewer.avatarUrl}
                  alt={`${reviewer.username}의 아바타`}
                  className="w-5 h-5 rounded-full mr-1 cursor-pointer"
                />
              </Tooltip>
            </>
          ) : (
            <span className="text-xs text-gray-500"></span>
          )}
        </div>
      </div>
      <div className="mt-2 text-xs text-gray-600 flex justify-between">
        <div>
          <LabelList labels={labels} projectLabels={projectLabels} />
        </div>
        <div className="text-xs mr-1">
          <span>updated {updateTimeAgo}</span>
        </div>
      </div>
    </div>
  );
}
